package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.UserNotUnauthorizedException;
import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandlerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBadRequestException(BadRequestException ex) {
        LOGGER.warn(ex.getExceptionMessage(), ex.getStatusCode());
        return new ModelAndView("redirect:/400");
    }

    @ExceptionHandler(UserNotUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleNotUnauthorizedException(UserNotUnauthorizedException ex) {
        LOGGER.warn(ex.getExceptionMessage(), ex.getStatusCode());
        return new ModelAndView("/user/demand_auth");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        LOGGER.warn(ex.getExceptionMessage(), ex.getStatusCode());
        return new ModelAndView("redirect:/404");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ModelAndView("redirect:/500");
    }
}
