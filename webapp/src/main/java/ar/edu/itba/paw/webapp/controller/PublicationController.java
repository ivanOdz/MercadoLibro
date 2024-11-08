package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import ar.edu.itba.paw.webapp.form.ExchangeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PublicationController {

    @Autowired
    private PublicationService ps;

    @Autowired
    private BookService bs;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationController.class);

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "search", defaultValue = "") String search,
                              @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") String isBookStateFilterActive,
                              @RequestParam(name = "book-state-filter", required = false) String bookStateFilter,
                              @RequestParam(name = "is-genre-filter-active", defaultValue = "false") String isGenreFilterActive,
                              @RequestParam(name = "genre-filter", required = false) String genreFilter,
                              @RequestParam(name = "order", defaultValue = "sort.publication.date.ascending") String sortType,
                              @RequestParam(name = "page", defaultValue = "0") String currentPage, @ModelAttribute("loggedUser") User loggeduser) {

        final ModelAndView mav = new ModelAndView("home/publications");

        PaginatedResponse<Publication, ItemFilterMetadata> publications = ps.getPaginatedPublications(search, isBookStateFilterActive,
                    bookStateFilter, isGenreFilterActive, genreFilter, sortType, currentPage, loggeduser);

        List<GenreWrapper> genreWrapperList = ps.getGenreWrapperList(search, isBookStateFilterActive, bookStateFilter);
        List<BookStateWrapper> bookStateWrapperList = ps.getBookStateWrapperList(search, isGenreFilterActive, genreFilter);

        mav.addObject("publications", publications);
        mav.addObject("genreWrapperList", genreWrapperList);
        mav.addObject("bookStateWrapperList", bookStateWrapperList);

        return mav;
    }

    @PostMapping(path = "/createpublication")
    public ModelAndView createPublication(@RequestParam(name = "bookId") long bookId, @RequestParam(name = "locationId") long locationId, @ModelAttribute("loggedUser") User loggeduser) {
    	
        try {
            ps.createPublication(bookId, loggeduser.getUserId(), locationId, PublicationState.CURRENT);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        LOGGER.info(messageSource.getMessage("info.publication.created", null, LocaleContextHolder.getLocale()));

        return new ModelAndView("redirect:/book");
    }

    @GetMapping("/publications/{publication_id:\\d+}")
    public ModelAndView publicationDetail(@PathVariable(name = "publication_id") long publicationId, @ModelAttribute("loggedUser") User loggeduser) {
        final ModelAndView mav = new ModelAndView("/home/publication_detail");

        Publication publication;
        try{
            publication = ps.getPublicationByPublicationId(publicationId);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        List<Book> availableBooks;
        mav.addObject("user", loggeduser);
        if (loggeduser != null) {
            availableBooks = bs.getAvailableBooksByUser(loggeduser);
            mav.addObject("availableBooks", availableBooks);
        }

        mav.addObject("exchangeForm", new ExchangeForm());
        mav.addObject("publication", publication);
        mav.addObject("imgCount", publication.getBook().getImages().size());
        mav.addObject("genres", Genre.values());

        return mav;
    }

    @RequestMapping(path = "/user_auth")
    public ModelAndView forceUserAuth() {
        return new ModelAndView("/user/demand_auth");
    }

    @RequestMapping(path = "/my_publications")
    public ModelAndView myPublications(@RequestParam(name = "search", defaultValue = "") String search,
                                       @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") String isBookStateFilterActive,
                                       @RequestParam(name = "book-state-filter", required = false) String bookStateFilter,
                                       @RequestParam(name = "is-genre-filter-active", defaultValue = "false") String isGenreFilterActive,
                                       @RequestParam(name = "genre-filter", required = false) String genreFilter,
                                       @RequestParam(name = "order", defaultValue = "sort.publication.date.ascending") String sortType,
                                       @RequestParam(name = "page", defaultValue = "0") String currentPage,
                                       @ModelAttribute("loggedUser") User loggeduser) {

        PaginatedResponse<Publication, ItemFilterMetadata> publications = ps.getMyPaginatedPublications(loggeduser.getUserId(), search, isBookStateFilterActive,
                bookStateFilter, isGenreFilterActive, genreFilter, sortType, currentPage);
        ModelAndView mav = new ModelAndView("/home/my_publications");

        List<GenreWrapper> genreWrapperList = ps.getMyGenreWrapperList(loggeduser.getUserId(), search, isBookStateFilterActive, bookStateFilter);
        List<BookStateWrapper> bookStateWrapperList = ps.getMyBookStateWrapperList(loggeduser.getUserId(), search, isGenreFilterActive, genreFilter);

        mav.addObject("publications", publications);
        mav.addObject("sort-types", SortType.values());
        mav.addObject("genreWrapperList", genreWrapperList);
        mav.addObject("bookStateWrapperList", bookStateWrapperList);

        return mav;
    }

    @RequestMapping(path = "/my_favorites")
    public ModelAndView myFavoritePublications(@RequestParam(name = "page", defaultValue = "0") String currentPage, @ModelAttribute("loggedUser") User loggeduser) {

        PaginatedResponse<Publication, BasicMetadata> publications = ps.getFavoritePublications(loggeduser, currentPage);
        ModelAndView mav = new ModelAndView("/home/favorite_publications");

        mav.addObject("publications", publications);

        return mav;
    }

    @GetMapping("/publications/{publication_id:\\d+}/delete")
    public ModelAndView deletePublication(@PathVariable(name = "publication_id") long publicationId, @ModelAttribute("loggedUser") User loggeduser) {
        Publication publication = ps.getPublicationByPublicationId(publicationId);
        if(publication.getUser().getUserId() != loggeduser.getUserId()) {
            return new ModelAndView("redirect:/403");
        }
        ps.deletePublication(publicationId);
        return new ModelAndView("redirect:/my_publications");
    }

    @PostMapping("/like/{publicationId:\\d+}")

    public ModelAndView likePublication(@PathVariable(name = "publicationId") long publicationId, @RequestParam(name = "fromFavorites", defaultValue = "false") boolean fromFavorites, @ModelAttribute("loggedUser") User loggeduser) {
        ps.likePublication(publicationId, loggeduser.getUserId());
        if(fromFavorites) {
            return new ModelAndView("redirect:/my_favorites");
        }
        return new ModelAndView("redirect:/");
    }



}
