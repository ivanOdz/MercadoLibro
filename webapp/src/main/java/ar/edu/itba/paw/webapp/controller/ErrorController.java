package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

//    @Autowired
//    private final MessageSource messageSource;
//
//    public ErrorController(MessageSource messageSource) {
//        this.messageSource = messageSource;
//    }
//
    @RequestMapping("/400")
    public ModelAndView badRequest() {
        return new ModelAndView("error/400");
    }

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        return new ModelAndView("error/403");
    }

    @RequestMapping("/404")
    public ModelAndView notFound() {
        return new ModelAndView("error/404");
    }

    @RequestMapping("/500")
    public ModelAndView internalServerError() {
        return new ModelAndView("error/internalservererror");
    }
//
//    @RequestMapping("/**")
//    public ModelAndView handleAll() {
//        return new ModelAndView("error/notFound");
//    }
}
