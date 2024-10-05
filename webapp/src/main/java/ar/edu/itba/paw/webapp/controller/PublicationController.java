package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.models.utils.pagination.Metadata;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;

import ar.edu.itba.paw.webapp.form.ExchangeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PublicationController {

    private final PublicationService ps;
    private final LocationService ls;
    private final UserService us;
    private final BookService bs;

    @Autowired
    private GenreService genreService;
    @Autowired
    private BookStateService bookStateService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public PublicationController(PublicationService ps, LocationService ls, UserService us, BookService bs) {
        this.ps = ps;
        this.ls = ls;
        this.us = us;
        this.bs = bs;
    }

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "search", defaultValue = "") String search,
                              @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") boolean isBookStateFilterActive,
                              @RequestParam(name = "book-state-filter", required = false) BookState bookStateFilter,
                              @RequestParam(name = "is-genre-filter-active", defaultValue = "false") boolean isGenreFilterActive,
                              @RequestParam(name = "genre-filter", required = false) Genre genreFilter,
                              @RequestParam(name = "sort-type", defaultValue = "PUBLICATION_DATE_ASCENDING") SortType sortType,
                              @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {

        final ModelAndView mav = new ModelAndView("home/publications");

        PaginatedResponse<Publication, ItemFilterMetadata> publications = ps.getPaginatedPublications(search, isBookStateFilterActive,
                bookStateFilter, isGenreFilterActive, genreFilter, sortType, currentPage);

        mav.addObject("publications", publications);

        return mav;
    }

    @PostMapping(path = "/createpublication")
    public ModelAndView createPublication(@RequestParam(name = "bookId") long bookId, @RequestParam(name = "location") String location) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            ps.createPublication(bookId, pud.getUser().getUserId(), location, PublicationState.CURRENT);
        }

        return new ModelAndView("redirect:/book");
    }



    /*@RequestMapping("/publication")
    public ModelAndView publication(@RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/publication");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = us.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }
        if (ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        } else {
            mav.addObject("publication", ps.getPublicationById(publicationId).get());
        }

        return mav;
    }*/

@GetMapping("/publications/{publication_id:\\d+}")
public ModelAndView publicationDetail(@PathVariable(name = "publication_id") long publicationId) {
    final ModelAndView mav = new ModelAndView("/home/publication_detail");
    Publication publication = ps.getPublicationByPublicationId(publicationId);
    List<Book> availableBooks;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal() instanceof PawUserDetails pud) {
        User loggedUser = us.findById(pud.getUser().getUserId()).get();
        mav.addObject("loggedUser", loggedUser);

        availableBooks = bs.getAvailableBooksByUser(pud.getUser());
        mav.addObject("availableBooks", availableBooks);
    }
    if (publication == null) {
        // TODO: Hace vista que la publicacion ya no esta disponible
        return new ModelAndView("error/forbidden");
    }

    mav.addObject("exchangeForm", new ExchangeForm());
    mav.addObject("publication", publication);
    mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
    return mav;
}

// Esto tienen que volar
    /*@RequestMapping("/submitmail")
    public ModelAndView submitMail(@RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/submitmail");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = us.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }


        if (ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        } else mav.addObject("publication_id", publicationId);
        return mav;
    }*/

    /*@RequestMapping(value = "/submitmail", method = RequestMethod.POST)
    public ModelAndView handleMailSubmission(@RequestParam(name = "submited_mail") String submited_mail, @RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/comparemail");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = us.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

        if (ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }

        long userId = ps.getPublicationById(publicationId).get().getUserId();
        User owner = us.findById(userId).get();

        mav.addObject("ownerMail", owner.getMail());
        mav.addObject("submited_mail", submited_mail);
        mav.addObject("publication_id", publicationId);
        mav.addObject("is_for_exchange", true);

        return mav;
    }*/

}
