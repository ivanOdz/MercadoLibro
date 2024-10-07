package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.UserNotUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;



@ControllerAdvice
public class GlobalExceptionHandlerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
/*
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex) {
        ModelAndView mav = new ModelAndView("error/generic");
        mav.addObject("errorMessage", "Ocurrió un error inesperado: " + ex.getMessage());
        return mav;
    }*/

    @ExceptionHandler(UserNotUnauthorizedException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex) {
        LOGGER.warn("A user tried to access a page without being logged in. Redirecting to auth page.");
        return new ModelAndView("/user/demand_auth");
    }
}
