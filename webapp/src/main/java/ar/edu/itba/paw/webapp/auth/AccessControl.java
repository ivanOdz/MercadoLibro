package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.ExchangeService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class AccessControl {

    @Autowired
    private PawUserDetailsService pawUserDetailsService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private LocationService locationService;

    public Boolean exchangeUserAccess(HttpServletRequest request) {
        long userId = Long.parseLong(request.getParameter("id"));
        return getUser().getUserId().equals(userId);
    }

    public Boolean booksAccess(HttpServletRequest request) {
        long userId = Long.parseLong(request.getParameter("owner"));
        return getUser().getUserId().equals(userId);
    }

    public Boolean modifyBookAccess(HttpServletRequest request, Long id) {
        long userId = getUser().getUserId();

        Book b = bookService.getBookById(id);
        return b.getOwner().getUserId().equals(userId);
    }

    public Boolean exchangeAccess(HttpServletRequest request) {
        long exchangeId = Long.parseLong(request.getParameter("id"));
        Exchange e = exchangeService.getExchangeById(exchangeId);

        long userId = getUser().getUserId();

        // userId matches requester or offerer id
        return e.getRequester().getUser().getUserId().equals(userId) || e.getOfferer().getUser().getUserId().equals(userId);
    }

    public Boolean createExchangeAccess(HttpServletRequest request) {
        // user must be the owner of the book
        Book b = bookService.getBookById(Long.parseLong(request.getParameter("book")));

        // location must be in the user's locations
        Location l = locationService.findById(Long.parseLong(request.getParameter("location")));

        return b.getOwner().getUserId().equals(getUser().getUserId()) &&
                l.getUsers().contains(getUser());
    }

    public Boolean exchangeUpdateAccess(HttpServletRequest request, Long id) {
        Exchange e = exchangeService.getExchangeById(id);
        User lu = getUser();

        Boolean requester = Boolean.parseBoolean(request.getParameter("requester"));
        Boolean accepted = Boolean.parseBoolean(request.getParameter("accepted"));

        if(accepted != null){
            return getUser().getUserId().equals(e.getOfferer().getUser().getUserId());
        }

        if(requester != null){
            return requester ? lu.getUserId().equals(e.getRequester().getUser().getUserId()) : lu.getUserId().equals(e.getOfferer().getUser().getUserId());
        }

        return false;
    }

    public Boolean exchangeRequesterAccess(HttpServletRequest request, Long id) {
        Long loggedUserId = getUser().getUserId();
        Long userId = Long.parseLong(request.getParameter("user-id"));
        return exchangeService.getExchangeById(id).getRequester().getUser().getUserId().equals(loggedUserId)
                && userId.equals(loggedUserId);
    }

    public Boolean exchangeOffererAccess(HttpServletRequest request, Long id) {
        Long loggedUserId = getUser().getUserId();
        Long userId = Long.parseLong(request.getParameter("user-id"));

        return exchangeService.getExchangeById(id).getOfferer().getUser().getUserId().equals(loggedUserId)
                && userId.equals(loggedUserId);
    }



    private User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
