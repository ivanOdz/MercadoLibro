package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

    //@RequestMapping(path = "/", method = RequestMethod.GET, (entrar a la documentacion)
    @RequestMapping("/")
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView("index.jsp");
        mav.addObject("username", "PAW");
        return mav;
    }
}