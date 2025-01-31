package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    @Autowired
    private UserReviewService userReviewService;

    // Book

    public Boolean booksAccess(HttpServletRequest request) {
        long userId = Long.parseLong(request.getParameter("owner"));
        return getUser().getUserId().equals(userId);
    }

    //#
    public Boolean modifyBookAccess(HttpServletRequest request, Long id) {
        long userId = getUser().getUserId();
        Optional<Book> b = bookService.getBookById(id);

        return b.map(book -> book.getOwner().getUserId().equals(userId)).orElse(true);
    }

    // Book model



    // FIXME: id in endpoint should be uri
    public Boolean exchangeUserAccess(HttpServletRequest request) {
        long userId = Long.parseLong(request.getParameter("id"));
        return getUser().getUserId().equals(userId);
    }




    // FIXME: id in endpoint should be uri
    public Boolean exchangeAccess(HttpServletRequest request) {
        long exchangeId = Long.parseLong(request.getParameter("id"));
        Optional<Exchange> e = exchangeService.getExchangeById(exchangeId);

        long userId = getUser().getUserId();

        // userId matches requester or offerer id
        if (e.isPresent()) {
        	return e.get().getRequester().getUser().getUserId().equals(userId) || e.get().getOfferer().getUser().getUserId().equals(userId);
        }
        return false;
    }

    // FIXME: ids in endpoint should be uri
    public Boolean createExchangeAccess(HttpServletRequest request) {
        // user must be the owner of the book
        Optional<Book> b = bookService.getBookById(Long.parseLong(request.getParameter("book")));

        // location must be in the user's locations
        Location l = locationService.findById(Long.parseLong(request.getParameter("location")));

        return b.filter(book -> book.getOwner().getUserId().equals(getUser().getUserId()) &&
                l.getUsers().contains(getUser())).isPresent();

    }

    public Boolean exchangeUpdateAccess(HttpServletRequest request, Long id) {
        Optional<Exchange> e = exchangeService.getExchangeById(id);
        User lu = getUser();

        Boolean requester = Boolean.parseBoolean(request.getParameter("requester"));
        Boolean accepted = Boolean.parseBoolean(request.getParameter("accepted"));

        if (e.isEmpty()) {
        	return null;
        }
        
        if(accepted != null ){
            return getUser().getUserId().equals(e.get().getOfferer().getUser().getUserId());
        }

        if(requester != null){
            return requester ? lu.getUserId().equals(e.get().getRequester().getUser().getUserId()) : lu.getUserId().equals(e.get().getOfferer().getUser().getUserId());
        }

        return false;
    }

    // CHECK: publication access could be different if accessed from library
    public Boolean publicationAccess(HttpServletRequest request, Long id) {
        Optional<Publication> p = publicationService.getPublicationByPublicationId(id);
        User lu = getUser();

        if(p.get().getPublicationState() == PublicationState.CURRENT){
            return true;
        }
        return p.get().getUser().getUserId().equals(lu.getUserId());
    }

    // FIXME: user id in endpoint should be sent as an uri
    public Boolean publicationsPostAccess(HttpServletRequest request, Long publicationId) {
        // IMPLEMENT:  user id in uri matches logged user id
        Long userId = Long.parseLong(request.getParameter("user"));
        return getUser().getUserId().equals(userId);
    }

    public Boolean publicationsModifyAccess(HttpServletRequest request, Long publicationId) {
        Optional<Publication> p = publicationService.getPublicationByPublicationId(publicationId);
        return getUser().getUserId().equals(p.get().getUser().getUserId());
    }

    // FIXME: user id in endpoint should be sent as an uri
    public Boolean publicationsGeneralAccess(HttpServletRequest request, Long publication_id) {
        boolean favorite = Boolean.parseBoolean(request.getParameter("favorite"));

        // IMPLEMENT:  favorite is marked then check if userId matches logged user

        if(!favorite) return true;

        // ASK : could this be just an .authenticated()?
        return getUser() != null;

    }

    public Boolean userAccess(HttpServletRequest request, Long id) {
        return getUser().getUserId().equals(id);
    }


    public Boolean reviewAccess(HttpServletRequest request, Long id, Long userReviewId) {
        UserReview ur = userReviewService.findUserReviewById(id, userReviewId);
        return getUser().getUserId().equals(ur.getReviewer().getUserId());
    }

    private User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
