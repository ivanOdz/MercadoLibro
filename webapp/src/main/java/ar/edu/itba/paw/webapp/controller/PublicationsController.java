package ar.edu.itba.paw.webapp.controller;

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

@Controller
public class PublicationsController {

    private PublicationsService ps;
    private BookService bs;
    private UserService us;

    public PublicationsController(final PublicationsService ps, final BookService bs, final UserService us) {
        this.ps = ps;
        this.bs = bs;
        this.us = us;
    }

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "search", defaultValue = "") String search) {
        final ModelAndView mav = new ModelAndView("home/publications");
        mav.addObject("publications", ps.getAllPublicationsFilteredBy(search));
        return mav;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam(name = "search", defaultValue = "") String search) {
        return index(search);
    }


        @RequestMapping("/publication")
    public ModelAndView publication(@RequestParam(name = "publicationId") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/publication");
        if(ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        else mav.addObject("publication", ps.getPublicationById(publicationId).get());
        return mav;
    }

    @RequestMapping("/submitmail")
    public ModelAndView submitMail(@RequestParam(name = "publicationId") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/submitmail");
        if(ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        else mav.addObject("publicationId", publicationId);
        return mav;
    }

    @RequestMapping(value = "/submitmail", method = RequestMethod.POST)
    public ModelAndView handleMailSubmission(@RequestParam(name = "email") String email, @RequestParam(name = "publicationId") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/comparemail");

        if (ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        User owner = us.findById(bs.getBookById(ps.getPublicationById(publicationId).get().getBookId()).get().getUserId()).get();

        mav.addObject("ownerMail", owner.getMail());
        mav.addObject("solicitingEmail", email);
        mav.addObject("publicationId", publicationId);
        mav.addObject("isForExchange", true);

        return mav;
    }

}
