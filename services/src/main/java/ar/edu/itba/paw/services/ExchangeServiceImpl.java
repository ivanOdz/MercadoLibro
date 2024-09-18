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
import java.util.stream.Collectors;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeDao exchangeDao;
    private final BookService bookService;
    private final BookModelService bookModelService;
    private final ImageService imageService;
    private final PublicationService publicationService;
    private final BookAuthorService bookAuthorService;
    private final BookImageService bookImageService;
    private final LocationService locationService;
    private final UserService userService;
    private final EmailService emailService;
    private final UserReviewService userReviewService;

    @Value("#{environment.webappUrl}")
    private String webappUrl;

    public ExchangeServiceImpl(final ExchangeDao exchangeDao, BookService bookService, BookModelService bookModelService, ImageService imageService, PublicationService publicationService, BookAuthorService bookAuthorService, BookImageService bookImageService, LocationService locationService, UserService userService, EmailService emailService, UserReviewService userReviewService){
        this.exchangeDao = exchangeDao;
        this.bookService = bookService;
        this.bookModelService = bookModelService;
        this.imageService = imageService;
        this.publicationService = publicationService;
        this.bookAuthorService = bookAuthorService;
        this.bookImageService = bookImageService;
        this.locationService = locationService;
        this.userService = userService;
        this.emailService = emailService;
        this.userReviewService = userReviewService;
    }

    @Override
    public Optional<Exchange> getExchangeById(long exchangeId) {
        return exchangeDao.findById(exchangeId);
    }

    @Override
    public long getId(int acceptCode) {
        return exchangeDao.getIdByAcceptCode(acceptCode);
    }

    @Override
    public String exchange(int acceptCode, boolean state) {
        switch (exchangeDao.exchange(acceptCode, state)){
            case ResponseState.ACCEPTED: {
                return "exchange/accepted";
            }
            case ResponseState.REJECTED: {
                return "exchange/rejected";
            }
            default: return "exchange/invalid";
        }
    }

    @Override
    public void initializeExchange(CompleteBook requesterComplete, long offererPubId) {
        // Insertar tupla de requester en publicacion con fecha actual y publicationState = 2 (OFFERER)

        long location = locationService.newLocation(requesterComplete.getLocation());

        //System.out.println("Location = " + location);
        long requesterId = bookService.getBookById(requesterComplete.getSelectedBookId()).get().getOwnerId();
        long requesterPubId = publicationService.createPublication(requesterComplete.getSelectedBookId(), requesterId, location, PublicationState.OFFERED);

        Random random = new Random();
        int acceptCode = Math.abs(random.nextInt());
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        Exchange ex = exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode, timestamp);

        Map<String, Object> variables = new HashMap<>();
        Publication offererPub = publicationService.getPublicationById(ex.getOffererPubId()).get();
        Publication requesterPub = publicationService.getPublicationById(ex.getRequesterPubId()).get();

        Book bookOffered = bookService.getBookById(offererPub.getBookId()).get();
        Book bookRequested = bookService.getBookById(requesterPub.getBookId()).get();

        User oferrer = userService.findById(offererPub.getUserId()).get();
        User requester = userService.findById(requesterPub.getUserId()).get();

        String oferrerEmail = oferrer.getMail();

        String bookModelOfferedTitle = bookModelService.getBookModelByBookModelId(bookOffered.getBookModelId()).getTitle();
        String bookModelRequestedTitle = bookModelService.getBookModelByBookModelId(bookRequested.getBookModelId()).getTitle();


        variables.put("requesterEmail", requester.getMail());
        variables.put("requesterName", requester.getUsername());
        variables.put("requestedPublication", bookModelRequestedTitle);
        variables.put("offeredPublication", bookModelOfferedTitle);
        variables.put("validationUrl", webappUrl + "/createexchange?accept_code=" + ex.getAcceptCode() + "&state=true");
        variables.put("rejectionUrl", webappUrl + "/createexchange?accept_code=" + ex.getAcceptCode() +"&state=false");

        emailService.sendEmail(oferrerEmail, variables, "exchangeRequest", "Requesting");

    }


    /**
     *
     * @param userId
     * @return function called from the offered section of exchanges
     */
    @Override
    public List<ExchangeWrapper> getExchangeOffererWrapperListByUserId(long userId) {

        List<Exchange> exchanges = exchangeDao.getExchangesWhereUserIdIsOfferer(userId);

        return getExchangeWrapper(exchanges);
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
     * @param userId
     * @return function called from the solicited section of exchanges
     */
    @Override
    public List<ExchangeWrapper> getExchangeRequesterWrapperListByUserId(long userId) {

        List<Exchange> exchanges = exchangeDao.getExchangesWhereUserIdIsRequester(userId);

        return getExchangeWrapper(exchanges);
    }


    /**
     *
     * @param exchanges
     * @return the exchange data as it is shown on the table 'exchanges'. This function is called from the exchanges sections with the
     * exchanges already filtered depending on whether the user is an offerer or a solicitor
     */
    private List<ExchangeWrapper> getExchangeWrapper(List<Exchange> exchanges){
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
    }
}
