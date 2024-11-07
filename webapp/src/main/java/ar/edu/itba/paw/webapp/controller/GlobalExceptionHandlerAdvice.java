package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.UserNotUnauthorizedException;
import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandlerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,// este tampoco
            MissingServletRequestParameterException.class, //puede volar
            IllegalArgumentException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Marcado para que Spring responda con 400 automáticamente
    public ModelAndView handleSpringBadRequestExceptions(BadRequestException ex) {
        LOGGER.warn("Solicitud incorrecta: {}", ex.getMessage(), ex);
        return new ModelAndView("error/400"); // Retorna la vista de error 400 sin redirección
    }
/*
    @ExceptionHandler(UserNotUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleNotUnauthorizedException(UserNotUnauthorizedException ex) {
        LOGGER.warn(ex.getExceptionMessage(), ex.getStatusCode());
        return new ModelAndView("/user/demand_auth");
    }*/

    @ExceptionHandler({
            NoHandlerFoundException.class,
            NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)  // Marcado para que Spring responda con 400 automáticamente
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        LOGGER.warn(ex.getExceptionMessage(), ex.getStatusCode());
        return new ModelAndView("error/404");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ModelAndView("error/500");
    }
}
