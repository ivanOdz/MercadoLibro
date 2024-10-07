package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class PublicationController {

    private final PublicationService ps;
    private final BookService bs;

    @Autowired
    private GenreService genreService;

    @Autowired
    private LoggedUserAdvice loggedUserAdvice;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    public PublicationController(PublicationService ps, BookService bs) {
        this.ps = ps;
        this.bs = bs;
    }

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "search", defaultValue = "") String search,
                              @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") boolean isBookStateFilterActive,
                              @RequestParam(name = "book-state-filter", required = false) BookState bookStateFilter,
                              @RequestParam(name = "is-genre-filter-active", defaultValue = "false") boolean isGenreFilterActive,
                              @RequestParam(name = "genre-filter", required = false) Genre genreFilter,
                              @RequestParam(name = "sort-type", defaultValue = "PUBLICATION_DATE_ASCENDING") SortType sortType,
                              @RequestParam(name = "page", defaultValue = "0") int currentPage) {

        final ModelAndView mav = new ModelAndView("home/publications");

        PaginatedResponse<Publication, ItemFilterMetadata> publications = ps.getPaginatedPublications(search, isBookStateFilterActive,
                bookStateFilter, isGenreFilterActive, genreFilter, sortType, currentPage);

        mav.addObject("publications", publications);

        return mav;
    }

    @PostMapping(path = "/createpublication")
    public ModelAndView createPublication(@RequestParam(name = "bookId") long bookId, @RequestParam(name = "location") String location) {

        try {
            ps.createPublication(bookId, loggedUserAdvice.getLoggedUser().getUserId(), location, PublicationState.CURRENT);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        LOGGER.info(messageSource.getMessage("info.publication.created", null, LocaleContextHolder.getLocale()));

        return new ModelAndView("redirect:/book");
    }

    @GetMapping("/publications/{publication_id:\\d+}")
    public ModelAndView publicationDetail(@PathVariable(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("/home/publication_detail");

        Publication publication;
        try{
            publication = ps.getPublicationByPublicationId(publicationId);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        List<Book> availableBooks;
        User user = loggedUserAdvice.getLoggedUser();
        mav.addObject("user", user);
        if (user != null) {
            // IMPLEMENT: excepción no implementada, si queda páginada no hace falta una excepción, únicamente un checkeo en el jsp
            availableBooks = bs.getAvailableBooksByUser(user);
            mav.addObject("availableBooks", availableBooks);
        }

        mav.addObject("exchangeForm", new ExchangeForm());
        mav.addObject("publication", publication);
        mav.addObject("imgCount", publication.getBook().getImages().size());
        mav.addObject("genres", Stream.of(Genre.values()).map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
        return mav;
    }

    @RequestMapping(path = "/user_auth")
    public ModelAndView forceUserAuth() {
        return new ModelAndView("/user/demand_auth");
    }

}
