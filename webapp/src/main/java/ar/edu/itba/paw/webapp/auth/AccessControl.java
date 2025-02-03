package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.webapp.dto.input.*;
import ar.edu.itba.paw.webapp.dto.output.BookDTO;
import ar.edu.itba.paw.webapp.dto.output.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class AccessControl {

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

    // GET {base_path}/books?owner={id}
    public Boolean booksAccess(HttpServletRequest request) {
        long userId = Long.parseLong(request.getParameter("owner"));
        return getUser().getUserId().equals(userId);
    }

    // POST {base_path}/books  body: bookInputDTO
    public Boolean bookCreationAccess(BookInputDTO bookInputDTO) {
        return getUser().getUserId().equals(bookInputDTO.getUserId());
    }

    // PATCH {base_path}/books/{id} body: bookDTO
    public Boolean modifyBookAccess(Long id, BookDTO bookDTO) {
        long userId = getUser().getUserId();
        Book b = bookService.getBookById(id);

        return b.getOwner().getUserId().equals(userId) &&
                bookDTO.getOwnerId().equals(userId);
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

    // PATCH {base_path}/exchanges/{id} body: updateExchangeDTO
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


    // Publications

    // GET {base_path}/publications?favorites=true&userId=1
    // GET {base_path}/publications?favorites=false&userId=1
    // GET {base_path}/publications?favorites=false&userId=1&locationId=1
    public Boolean publicationsGeneralAccess(HttpServletRequest request) {
        boolean favorite = Boolean.parseBoolean(request.getParameter("favorites"));
        String userString = request.getParameter("user_id");

        String locationString = request.getParameter("location_id");

        // publications filtered by location accessed if location belongs to logged user
        Location l = locationString == null ? null : locationService.findById(Long.valueOf(locationString));
        if(l != null){
            return l.getUsers().contains(getUser());
        }

        // main page publications with no logged user
        if(userString.isEmpty() && !favorite) return true;

        Long userId = Long.parseLong(userString);
        return userId.equals(getUser().getUserId());
    }

    // POST {base_path}/publications
    public Boolean publicationsPostAccess(PublicationInputDTO publicationCreationDTO) {
        Location l = locationService.findById(publicationCreationDTO.getLocationId());
        Book b = bookService.getBookById(publicationCreationDTO.getBookId());
        Long userId = publicationCreationDTO.getUserId();
        return getUser().getUserId().equals(userId) &&
                l.getUsers().contains(getUser()) &&
                b.getOwner().getUserId().equals(userId);
    }

    // DELETE {base_path}/publications/{publication_id}
    public Boolean publicationsModifyAccess(Long publicationId) {
        Publication p = publicationService.getPublicationByPublicationId(publicationId);
        return getUser().getUserId().equals(p.getUser().getUserId());
    }

    // GET {base_path}/publications/{publication_id}
    public Boolean publicationAccess(Long id) {
        Publication p = publicationService.getPublicationByPublicationId(id);
        User lu = getUser();

        if(p.getPublicationState() == PublicationState.CURRENT){
            return true;
        }

        return p.getUser().getUserId().equals(lu.getUserId());
    }

    // POST {base_path}/publications/{id}/favorite body: userDTO
    public Boolean publicationsFavoritePostAccess(Long publicationId, UserDTO userDTO) {
        Publication p = publicationService.getPublicationByPublicationId(publicationId);
        return getUser().getUserId().equals(p.getUser().getUserId()) &&
                userDTO.getSelf().equals(getUser().getUserId());
    }

    // GET {base_path}/publications/{publication_id}/favorite/{favorite_id}
    public Boolean publicationsFavoriteListAccess(Long publicationId, Long favoriteId) {
        FavoritePublication fp = publicationService.getFavoritePublicationById(favoriteId);
        Publication p = publicationService.getPublicationByPublicationId(publicationId);
        Long luId = getUser().getUserId();
        return luId.equals(fp.getUser().getUserId()) &&
                luId.equals(p.getUser().getUserId());
    }

    public Boolean publicationsFavoriteAccess(HttpServletRequest request,Long publicationId) {
        Publication p = publicationService.getPublicationByPublicationId(publicationId);
        String userIdString = request.getParameter("user_id");

        if (userIdString.isEmpty()) {
            return false;
        }

        Long luId = getUser().getUserId();
        return luId.equals(Long.parseLong(userIdString)) &&
                luId.equals(p.getUser().getUserId());
    }





    // Users

    // GET {base_path}/users/{id}
    // PATCH {base_path}/users/{id}
    // GET {base_path}/users/{id}/locations
    // POST {base_path}/users/{id}/locations
    // GET {base_path}/users/{id}/locations/{location_id}
    // DELETE {base_path}/users/{id}/locations/{location_id}
    public Boolean userAccess(Long id) {
        return getUser().getUserId().equals(id);
    }
    
    // POST {base_path}/users/{id}/reviews
    public Boolean createReviewAccess(Long id, ReviewInputDTO reviewInputDTO) {
        Exchange e = exchangeService.getExchangeById(reviewInputDTO.getExchangeId());
        Long luId = getUser().getUserId();

        // logged user is a participant of the exchange
        boolean exchangeAccess = e.getRequester().getUser().getUserId().equals(luId) ||
                e.getOfferer().getUser().getUserId().equals(luId);

        // exchangeAccess and not self review
        return !getUser().getUserId().equals(id) &&
                exchangeAccess;
    }

    // GET {base_path}/users/{id}/reviews
    public Boolean reviewListAccess(Long id) {
        return getUser().getUserId().equals(id);
    }

    // GET {base_path}/users/{id}/reviews/{ur_id}
    public Boolean reviewAccess(Long id, Long userReviewId) {
        UserReview ur = userReviewService.findUserReviewById(id, userReviewId);
        return getUser().getUserId().equals(ur.getReviewer().getUserId());
    }


    // private methods

    private User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
