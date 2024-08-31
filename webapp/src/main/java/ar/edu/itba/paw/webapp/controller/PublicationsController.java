package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.PublicationsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PublicationsController {

    private PublicationsService ps;

    public PublicationsController(final PublicationsService ps) {
        this.ps = ps;
    }

    @RequestMapping("/home")
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView("home/publications");
        mav.addObject("publications", ps.getAllPublications());
        return mav;
    }

}
