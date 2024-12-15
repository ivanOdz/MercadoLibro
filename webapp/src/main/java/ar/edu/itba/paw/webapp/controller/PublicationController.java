package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import ar.edu.itba.paw.webapp.form.ExchangeForm;
import ar.edu.itba.paw.webapp.form.LocationForm;
import ar.edu.itba.paw.webapp.form.PublicationForm;
import org.springframework.beans.factory.annotation.Autowired;
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


    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "search", defaultValue = "") String search,
                              @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") String isBookStateFilterActive,
                              @RequestParam(name = "book-state-filter", required = false) String bookStateFilter,
                              @RequestParam(name = "is-genre-filter-active", defaultValue = "false") String isGenreFilterActive,
                              @RequestParam(name = "genre-filter", required = false) String genreFilter,
                              @RequestParam(name = "order", defaultValue = "sort.publication.date.descending") String sortType,
                              @RequestParam(name = "page", defaultValue = "0") int currentPage, @ModelAttribute("loggedUser") User loggeduser) {

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
        ps.createPublication(bookId, loggeduser.getUserId(), locationId, PublicationState.CURRENT);
        return new ModelAndView("redirect:/book");
    }

    @PostMapping(path = "/book_home/createpublication")
    public ModelAndView createPublicationFromBookHome(@ModelAttribute PublicationForm publicationForm, @ModelAttribute("loggedUser") User loggeduser) {
        createPublication(publicationForm.getBookId(), publicationForm.getLocationId(), loggeduser);
        return new ModelAndView("redirect:/my_publications");
    }

    @GetMapping("/publications/{publication_id:\\d+}")
    public ModelAndView publicationDetail(@PathVariable(name = "publication_id") long publicationId, @ModelAttribute("loggedUser") User loggeduser) {
        final ModelAndView mav = new ModelAndView("/home/publication_detail");

        Publication publication = ps.getActivePublication(loggeduser, publicationId);

        if(publication == null) {
            return new ModelAndView("redirect:/404");
        }

        List<Book> availableBooks = bs.getAvailableBooksByUser(loggeduser);

        // Available books puede ser una lista vacia si es que el usuario no esta autenticado o no tiene libros
        mav.addObject("availableBooks", availableBooks);

        mav.addObject("publication", publication);
        mav.addObject("exchangeForm", new ExchangeForm());
        mav.addObject("locationForm", new LocationForm());
        mav.addObject("genres", Genre.values());
        mav.addObject("bookStates", BookState.values());

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
                                       @RequestParam(name = "page", defaultValue = "0") int currentPage,
                                       @ModelAttribute("loggedUser") User loggeduser) {

        PaginatedResponse<Publication, ItemFilterMetadata> publications = ps.getMyPaginatedPublications(loggeduser.getUserId(), search, isBookStateFilterActive,
                bookStateFilter, isGenreFilterActive, genreFilter, sortType, currentPage);
        ModelAndView mav = new ModelAndView("/home/my_publications");

        List<GenreWrapper> genreWrapperList = ps.getMyGenreWrapperList(loggeduser.getUserId(), search, isBookStateFilterActive, bookStateFilter);
        List<BookStateWrapper> bookStateWrapperList = ps.getMyBookStateWrapperList(loggeduser.getUserId(), search, isGenreFilterActive, genreFilter);

        mav.addObject("publications", publications);
        mav.addObject("genreWrapperList", genreWrapperList);
        mav.addObject("bookStateWrapperList", bookStateWrapperList);

        return mav;
    }

    @RequestMapping(path = "/my_favorites")
    public ModelAndView myFavoritePublications(@RequestParam(name = "page", defaultValue = "0") int currentPage, @ModelAttribute("loggedUser") User loggeduser) {
        PaginatedResponse<Publication, BasicMetadata> publications = ps.getFavoritePublications(loggeduser, currentPage);
        ModelAndView mav = new ModelAndView("/home/favorite_publications");

        mav.addObject("publications", publications);
        return mav;
    }

    @GetMapping("/publications/{publication_id:\\d+}/delete")
    public ModelAndView deletePublication(@PathVariable(name = "publication_id") long publicationId, @ModelAttribute("loggedUser") User loggeduser) {
        ps.deletePublication(loggeduser.getUserId(), publicationId);
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

    @PostMapping("/publication/add_location")
    public ModelAndView addLocation(@RequestParam(name = "publicationId") Long publicationId, @RequestParam(name = "locationId") long locationId, @ModelAttribute("loggedUser") User loggeduser) {
        ps.addLocation(publicationId, locationId, loggeduser);
        return new ModelAndView("redirect:/publications/" + publicationId);
    }
}
