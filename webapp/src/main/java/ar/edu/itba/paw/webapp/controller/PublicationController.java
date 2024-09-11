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

    @Autowired
    private GenreService genreService;
    @Autowired
    private BookStateService bookStateService;
    
    public PublicationController(PublicationService ps, UserService us, CardService cs, LocationService ls, BookModelService bms, CompleteBookService cbs) {
        this.ps = ps;
        this.us = us;
        this.cs = cs;
        this.ls = ls;
        this.bms = bms;
        this.cbs = cbs;
    }

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "search", defaultValue = "") String search) {
    	
        final ModelAndView mav = new ModelAndView("home/publications");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            List<Card> cardList = cs.buildCardList(ps.getAllPublicationsFilteredBy(search, pud.getUser().getUserId()));

            mav.addObject("username", pud.getUser().getUsername());
            mav.addObject("publications", cardList);


            mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
            mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));

        }
        
        return mav;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam(name = "search", defaultValue = "") String search) {
        return index(search);
    }

    @RequestMapping(path = "/createpublication", method = RequestMethod.POST)
    public ModelAndView createPublication(@ModelAttribute(name = "publicationForm")PublicationForm publicationForm/*, @RequestParam(name = "bookId") long bookId, @RequestParam(name = "location") String location*/){
        ModelAndView mav = new ModelAndView("book/book_home");
        mav.addObject("publicationForm", publicationForm);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            long locationId = ls.newLocation(publicationForm.getLocation());
            ps.createPublication(publicationForm.getBookId(), pud.getUser().getUserId(), locationId, PublicationState.CURRENT);
        }

        return mav;
    }


    @RequestMapping("/publication")
    public ModelAndView publication(@RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/publication");
        
        if (ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        else {
            mav.addObject("publication", ps.getPublicationById(publicationId).get());
        }

        return mav;
    }

    @RequestMapping("/publications/{publication_id:\\d+}")
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
        }
        return mav;
    }


    // Esto tienen que volar
    @RequestMapping("/submitmail")
    public ModelAndView submitMail(@RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/submitmail");
        if(ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        else mav.addObject("publication_id", publicationId);
        return mav;
    }

    @RequestMapping(value = "/submitmail", method = RequestMethod.POST)
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
    }

}
