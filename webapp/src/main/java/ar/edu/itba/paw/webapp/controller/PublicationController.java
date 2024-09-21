package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;

import ar.edu.itba.paw.webapp.form.PublicationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PublicationController {

    private final PublicationService ps;
    private final UserService us;
    private final CardService cs;
    private final LocationService ls;
    private final BookModelService bms;
    private final CompleteBookService cbs;
    private final PublicationDetailService pds;
    @Autowired
    private GenreService genreService;
    @Autowired
    private BookStateService bookStateService;
    
    public PublicationController(PublicationService ps, UserService us, CardService cs, LocationService ls, BookModelService bms, CompleteBookService cbs, PublicationDetailService pds) {
        this.ps = ps;
        this.us = us;
        this.cs = cs;
        this.ls = ls;
        this.bms = bms;
        this.cbs = cbs;
        this.pds = pds;
    }

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "search", defaultValue = "") String search,
                              @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") boolean isBookStateFilterActive,
                              @RequestParam(name = "book-state-filter", required = false) BookState bookStateFilter,
                              @RequestParam(name = "is-genre-filter-active", defaultValue = "false") boolean isGenreFilterActive,
                              @RequestParam(name = "genre-filter", required = false) Genre genreFilter,
                              @RequestParam(name = "page-index", defaultValue = "0") int pageIndex,
                              @RequestParam(name = "sort-type", defaultValue = "PUBLICATION_DATE_ASCENDING") SortType sortType) {

        final ModelAndView mav = new ModelAndView("home/publications");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            List<PublicationCard> publications = ps.getFilteredSortedOrderedPublicationsByPageExcludingUser(search, isBookStateFilterActive,
                    bookStateFilter, isGenreFilterActive, genreFilter, pageIndex, pud.getUser().getUserId(), sortType);

            mav.addObject("publications", publications);
            mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
            mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));
            mav.addObject("bookStateFilter", bookStateFilter);
            mav.addObject("isGenreFilterActive", isGenreFilterActive);
            mav.addObject("genreFilter", genreFilter);
            mav.addObject("isBookStateFilterActive", isBookStateFilterActive);

        }
        
        return mav;
    }
//
//    @RequestMapping(path = "/", method = RequestMethod.GET)
//    public ModelAndView search(@RequestParam(name = "search", defaultValue = "") String search,
//                               @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") boolean isBookStateFilterActive,
//                               @RequestParam(name = "book-state-filter", required = false) BookState bookStateFilter,
//                               @RequestParam(name = "is-genre-filter-active", defaultValue = "false") boolean isGenreFilterActive,
//                               @RequestParam(name = "genre-filter", required = false) Genre genreFilter,
//                               @RequestParam(name = "page-index", defaultValue = "0") int pageIndex,
//                               @RequestParam(name = "sort-type", defaultValue = "PUBLICATION_DATE_ASCENDING") SortType sortType) {
//
//        return index(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, pageIndex, sortType);
//    }

    @RequestMapping(path = "/createpublication", method = RequestMethod.POST)
    public ModelAndView createPublication(@RequestParam(name = "bookId") long bookId, @RequestParam(name = "location") String location){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            long locationId = ls.newLocation(location); // Esto se tiene que llamar dentro del publication service.
            ps.createPublication(bookId, pud.getUser().getUserId(), locationId, PublicationState.CURRENT);
        }

        return new ModelAndView("redirect:/book");
    }


    /*@RequestMapping("/publication")
    public ModelAndView publication(@RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/publication");
        
        if (ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        else {
            mav.addObject("publication", ps.getPublicationById(publicationId).get());
        }

        return mav;
    }*/

    /*@GetMapping("/publications/{publication_id:\\d+}")
    public ModelAndView publicationDetail(@PathVariable(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/publicationDetail");
        mav.addObject("card", cs.createCard(publicationId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {

            List<CompleteBook> completeBooks = cbs.getCompleteAvailableBooksByUserId(pud.getUser().getUserId());

            // Aquí estamos creando un objeto de modelo vacío
            CompleteBook completeBookParam = new CompleteBook(null, null);
            mav.addObject("completeBookParam", completeBookParam);
            mav.addObject("completeBooks", completeBooks);
            mav.addObject("publication_id", publicationId);
            mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
        }
            mav.addObject("pd", pds.getPublicationDetail(publicationId));
        return mav;
    }*/


    // Esto tienen que volar
    /*@RequestMapping("/submitmail")
    public ModelAndView submitMail(@RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/submitmail");
        if(ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        else mav.addObject("publication_id", publicationId);
        return mav;
    }*/

    /*@RequestMapping(value = "/submitmail", method = RequestMethod.POST)
    public ModelAndView handleMailSubmission(@RequestParam(name = "submited_mail") String submited_mail, @RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/comparemail");

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
