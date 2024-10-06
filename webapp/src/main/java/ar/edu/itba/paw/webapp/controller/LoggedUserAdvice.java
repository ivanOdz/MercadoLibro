package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class LoggedUserAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    // Binding false because it is a read-onlu attribute
    @ModelAttribute(name = "loggedUser", binding = false)
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            LOGGER.debug("Logged user is {}", pud.getUser());
            return pud.getUser();
        }
        return null;
    }
}
