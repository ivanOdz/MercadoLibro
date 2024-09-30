package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.ResponseState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeDao exchangeDao;

    private final BookService bs;
    private final PublicationService ps;
    private final EmailService emailService;


    public ExchangeServiceImpl(ExchangeDao exchangeDao, BookService bs, PublicationService ps, EmailService emailService) {
        this.exchangeDao = exchangeDao;
        this.bs = bs;
        this.ps = ps;
        this.emailService = emailService;
    }

    @Value("#{environment.webappUrl}")
    private String webappUrl;

    @Override
    public Optional<Exchange> getExchangeByAcceptCode(int acceptCode) {
        return exchangeDao.findByAcceptCode(acceptCode);
    }

    @Override
    public String exchange(int acceptCode, boolean state) {
        Optional<Exchange> ex = exchangeDao.exchange(acceptCode, state);

        if(ex.isEmpty()){
            // TODO: EXCEPTIONS
        }

        // --- email variables
        Map<String, Object> variables = new HashMap<>();
        Book bookOffered = ex.get().getOfferer().getBook();
        Book bookRequested = ex.get().getRequester().getBook();
        User requester = bookRequested.getOwner();
        User offerer = bookOffered.getOwner();

        variables.put("requestedBook", bookRequested.getBookModel().getTitle());
        variables.put("offeredBook", bookOffered.getBookModel().getTitle());
        variables.put("requesterEmail", requester.getMail());
        variables.put("requesterName", requester.getUsername());
        variables.put("offererName", offerer.getUsername());
        variables.put("offererEmail", offerer.getMail());

        emailService.sendExchangeEmail(requester.getMail(), variables, state);
        // ---


        bs.exchangeOwnership(bookOffered, bookRequested);

        ps.terminatePublication(ex.get().getOfferer());
        ps.terminatePublication(ex.get().getRequester());


        switch (ex.get().getExchangeState()){
            case ExchangeState.ACCEPTED:{
                return "exchange/accepted";
            }
            case ExchangeState.REJECTED: {
                return "exchange/rejected";
            }
            default: return "exchange/invalid";
        }
    }


    // TODO: initialize exchange
   /* @Override
    public void initializeExchange(CompleteBook requesterComplete, long offererPubId) {
        // Insertar tupla de requester en publicacion con fecha actual y publicationState = 2 (OFFERER)

        long location = locationService.newLocation(requesterComplete.getLocation());

        //System.out.println("Location = " + location);
//        long requesterId = bookService.getBookById(requesterComplete.getSelectedBookId()).get().getOwnerId();


        Publication requesterPub = ps.createPublication(requesterComplete, requesterId, location, PublicationState.OFFERED);

        Random random = new Random();
        int acceptCode = Math.abs(random.nextInt());

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Optional<Exchange> ex = exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode, timestamp);


        // mail variables setup

        Map<String, Object> variables = new HashMap<>();

        User offerer = ex.get().getOfferer().getBook().getOwner();
        User requester = ex.get().getRequester().getBook().getOwner();

        Book bookOffered = ex.get().getOfferer().getBook();
        Book bookRequested = ex.get().getRequester().getBook();

        variables.put("requesterEmail", requester.getMail());
        variables.put("requesterName", requester.getUsername());
        variables.put("requestedPublication", bookRequested.getBookModel().getTitle());
        variables.put("offeredPublication", bookOffered.getBookModel().getTitle());
        variables.put("validationUrl", webappUrl + "/createexchange?accept_code=" + ex.get().getAcceptCode() + "&state=true");
        variables.put("rejectionUrl", webappUrl + "/createexchange?accept_code=" + ex.get().getAcceptCode() +"&state=false");

        emailService.sendEmail(offerer.getMail(), variables, "exchangeRequest", "Requesting");

    }
*/

    // exchanges where user is the publication owner
    @Override
    public List<Exchange> getExchangeOffererListByUserId(long userId, ExchangeState exchangeState) {
        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, true);
    }

    // exchanges where user is the requester owner
    @Override
    public List<Exchange> getExchangeRequesterListByUserId(long userId, ExchangeState exchangeState) {
        return exchangeDao.getAllExchangesByUserId(userId, exchangeState, false);
    }

    @Override
    public void cofirmOfferer(int acceptCode) {
        exchangeDao.confirmOfferer(acceptCode);
    }

    @Override
    public void cofirmRequester(int acceptCode) {
        exchangeDao.confirmRequester(acceptCode);
    }

    /**
     *
     * @param exchanges
     * @return the exchange data as it is shown on the table 'exchanges'. This function is called from the exchanges sections with the
     * exchanges already filtered depending on whether the user is an offerer or a solicitor
     */
    /*private List<ExchangeWrapper> getExchangeWrapper(List<Exchange> exchanges){
        List<ExchangeWrapper> toReturn = new ArrayList<>();

        for (Exchange ex : exchanges) {

            // requester data
            String requesterLocation = locationService.getLocationByPublicationId(ex.getRequesterPubId());
            User requester = userService.getUserByPubId(ex.getRequesterPubId());
            String requesterMail = requester.getMail();
            String requesterUsername = requester.getUsername();
            // book - requested data
            Book requesterBook = bookService.getBookByPubId(ex.getRequesterPubId());
            BookModel requesterBookModel = bookModelService.getBookModelByBookModelId(requesterBook.getBookModelId());
            List<BookImage> requesterBookImages = bookImageService.getImageByBookId(requesterBook.getBookId());
            List<Author> requesterBookAuthor = bookAuthorService.getAuthorsByBookId(requesterBookModel.getBookModelId());
            List<String> requesterAuthorNames = requesterBookAuthor.stream()
                    .map(Author::getAuthorName)
                    .collect(Collectors.toList());


            // offerer data
            String offererLocation = locationService.getLocationByPublicationId(ex.getOffererPubId());
            User offerer = userService.getUserByPubId(ex.getOffererPubId());
            String offererMail = offerer.getMail();
            String offererUsername = offerer.getUsername();
            // book - offered data
            Book offererBook = bookService.getBookByPubId(ex.getOffererPubId());
            BookModel offererBookModel = bookModelService.getBookModelByBookModelId(offererBook.getBookModelId());
            List<BookImage> offererBookImages = bookImageService.getImageByBookId(offererBook.getBookId());


            List<Author> offererBookAuthor = bookAuthorService.getAuthorsByBookId(offererBookModel.getBookModelId());

            List<String> offererAuthorNames = offererBookAuthor.stream()
                    .map(Author::getAuthorName)
                    .collect(Collectors.toList());


            Boolean isReviewable = false;
            if ((ex.getExchangeState().equals(ExchangeState.ACCEPTED) || ex.getExchangeState().equals(ExchangeState.TERMINATED)) && userReviewService.getUserReview(ex.getExchangeId(), ex.getRequesterPubId()) == null) {
            	isReviewable = true;
            }

            toReturn.add(new ExchangeWrapper(ex, requesterLocation, requesterMail, requesterUsername, offererLocation, offererMail, offererUsername, offererBook, requesterBook, offererBookModel, requesterBookModel, requesterBookImages, offererBookImages, requesterAuthorNames, offererAuthorNames, isReviewable));
        }
        return toReturn;
    }*/



    /**
     *
     * @param userId
     * @return function called from the solicited section of exchanges
     */
//    @Override
//    public List<ExchangeWrapper> getExchangeRequesterWrapperListByUserId(long userId) {
//
//        List<Exchange> exchanges = exchangeDao.getExchangesWhereUserIdIsRequester(userId);
//
//        return getExchangeWrapper(exchanges);
//    }



    /*@Override
    public Optional<Exchange> getExchangeById(long exchangeId) {
        return exchangeDao.findById(exchangeId);
    }

    @Override
    public long getId(int acceptCode) {
        return exchangeDao.getIdByAcceptCode(acceptCode);
    }*/


}
