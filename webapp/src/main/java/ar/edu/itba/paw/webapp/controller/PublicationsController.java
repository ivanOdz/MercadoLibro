package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.PublicationsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PublicationsController {

    private PublicationsService ps;
    private BookService bs;

    public PublicationsController(final PublicationsService ps, final BookService bs) {
        this.ps = ps;
        this.bs = bs;
    }

    @RequestMapping("/")
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView("home/publications");
        mav.addObject("publications", ps.getAllPublications());
        return mav;
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

    @RequestMapping("/validation")
    public ModelAndView validation(@RequestParam(name = "publicationId") long publicationId) {
        final ModelAndView mav = new ModelAndView("home/validation");
        if(ps.getPublicationById(publicationId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publication not found");
        }
        else mav.addObject("book", bs.getBookById(ps.getPublicationById(publicationId).get().getBookId()).get());
        return mav;
    }
}
