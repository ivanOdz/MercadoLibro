package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.webapp.dto.input.CreateExchangeDTO;
import ar.edu.itba.paw.webapp.dto.input.MessageInputDTO;
import ar.edu.itba.paw.webapp.dto.input.UpdateExchangeDTO;
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

    // Books

    public Boolean booksAccess(HttpServletRequest request) {
        long userId = Long.parseLong(request.getParameter("owner"));
        return getUser().getUserId().equals(userId);
    }

    // CHECK
    public Boolean modifyBookAccess(Long id) {
        long userId = getUser().getUserId();
        Book b = bookService.getBookById(id);

        return b.getOwner().getUserId().equals(userId);
    }

    // Exchanges

    // GET {base_path}/exchanges?userId=1
    public Boolean exchangeUserAccess(HttpServletRequest request) {
        long userId = Long.parseLong(request.getParameter("user_id"));
        return getUser().getUserId().equals(userId);
    }

    // POST {base_path}/exchanges  body: createExchangeDTO
    public Boolean createExchangeAccess(CreateExchangeDTO createExchangeDTO) {
        // user must be the owner of the book
        Book b = bookService.getBookById(createExchangeDTO.getBookId());

        // location must be in the user's locations
        Location l = locationService.findById(createExchangeDTO.getLocationId());

        return b.getOwner().getUserId().equals(getUser().getUserId()) &&
                l.getUsers().contains(getUser());
    }

    // POST {base_path}/exchanges/{id}/messages  body: messageDTO
    public Boolean createMessageAccess(Long exchangeId, MessageInputDTO messageDTO) {
        Exchange e = exchangeService.getExchangeById(exchangeId);

        boolean exchangeAccess = e.getRequester().getUser().getUserId().equals(getUser().getUserId()) ||
                                    e.getOfferer().getUser().getUserId().equals(getUser().getUserId());

        return messageDTO.getUserId().equals(getUser().getUserId()) && exchangeAccess;
    }

    // GET  {base_path}/api/exchanges/{id:\\d+}"
    // GET  {base_path}/api/exchanges/{id:\\d+}/messages"
    // GET  {base_path}/api/exchanges/{id:\\d+}/messages/{message_id:\\d+}"
    public Boolean exchangeAccess(Long exchangeId, Long messageId) {
        Exchange e = exchangeService.getExchangeById(exchangeId);

        long userId = getUser().getUserId();

        // userId matches requester or offerer id
        boolean exchangeAccess = e.getRequester().getUser().getUserId().equals(userId) ||
                e.getOfferer().getUser().getUserId().equals(userId);

        // messageId is in exchange chat or not specified
        boolean messageInChat = messageId == null || e.getChat().stream().anyMatch(m -> m.getMessageId().equals(messageId));

        return exchangeAccess && messageInChat;
    }

    public Boolean exchangeUpdateAccess(Long id, UpdateExchangeDTO updateExchangeDTO) {
        Exchange e = exchangeService.getExchangeById(id);
        User lu = getUser();

        // to accept or reject an exchange the user must be the offerer
        if(updateExchangeDTO.getAccepted() != null ){
            return getUser().getUserId().equals(e.getOfferer().getUser().getUserId());
        }

        Boolean requester = updateExchangeDTO.getRequester();
        // to update the exchange as a requester user must be the requester
        if(requester != null){
            return requester ? lu.getUserId().equals(e.getRequester().getUser().getUserId()) : lu.getUserId().equals(e.getOfferer().getUser().getUserId());
        }

        // ASK : what if none of the fields are present?
        // bad request checked in service?
        return true;
    }


    // Publication

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
