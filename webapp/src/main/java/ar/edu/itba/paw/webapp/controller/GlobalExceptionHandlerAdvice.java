package ar.edu.itba.paw.webapp.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
/*
@ControllerAdvice
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex) {
        ModelAndView mav = new ModelAndView("error/generic");
        mav.addObject("errorMessage", "Ocurrió un error inesperado: " + ex.getMessage());
        return mav;
    }
}*/
