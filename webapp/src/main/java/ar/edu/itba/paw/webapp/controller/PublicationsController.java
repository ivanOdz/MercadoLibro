package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.CardService;
import ar.edu.itba.paw.models.Card;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.PublicationsService;
import ar.edu.itba.paw.interfaces.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PublicationsController {

    private PublicationsService ps;
    private BookService bs;
    private UserService us;
    private CardService cs;

    public PublicationsController(PublicationsService ps, BookService bs, UserService us, CardService cs) {
        this.ps = ps;
        this.bs = bs;
        this.us = us;
        this.cs = cs;
    }

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "search", defaultValue = "") String search) {
        final ModelAndView mav = new ModelAndView("home/publications");
        List<Card> cardList = cs.buildCardList(ps.getAllPublicationsFilteredBy(search).getPublications());
        mav.addObject("publications", cardList);
        return mav;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam(name = "search", defaultValue = "") String search) {
        return index(search);
    }


    @RequestMapping("/publication")
    public ModelAndView publication(@RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/publication");
        
        if (ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        else mav.addObject("publication", ps.getPublicationById(publicationId).get());
        
        return mav;
    }

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
