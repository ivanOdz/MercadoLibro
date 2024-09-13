package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.ResponseState;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public ExchangeServiceImpl(final ExchangeDao exchangeDao, BookService bookService, BookModelService bookModelService, ImageService imageService, PublicationService publicationService, BookAuthorService bookAuthorService, BookImageService bookImageService, LocationService locationService, UserService userService, EmailService emailService){
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

        System.out.println("Location = " + location);
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
        variables.put("validationUrl", "http://localhost:8080/createexchange?accept_code=" + ex.getAcceptCode() + "&state=true");
        variables.put("rejectionUrl", "http://localhost:8080/createexchange?accept_code=" + ex.getAcceptCode() +"&state=false");

        emailService.sendEmail(oferrerEmail, variables, "exchangeRequest", "Requesting");

    }


    @Override
    public List<ExchangeWrapper> getExchangeWrapperListByUserId(long userId) {
        List<ExchangeWrapper> toReturn = new ArrayList<>();

        List<Exchange> exchanges = exchangeDao.getExchangesByUserIdInvolved(userId);

        for (Exchange ex : exchanges) {
            String requesterLocation = locationService.getLocationByPublicationId(ex.getRequesterPubId());
            User requester = userService.getUserByPubId(ex.getRequesterPubId());
            String requesterMail = requester.getMail();
            String requesterUsername = requester.getUsername();
            Book offererBook = bookService.getBookByPubId(ex.getOffererPubId());
            Book requesterBook = bookService.getBookByPubId(ex.getOffererPubId());
            BookModel offererBookModel = bookModelService.getBookModelByBookModelId(offererBook.getBookModelId());
            BookModel requesterBookModel = bookModelService.getBookModelByBookModelId(requesterBook.getBookModelId());
            List<BookImage> requesterBookImages = bookImageService.getImageByBookId(requesterBook.getBookId());
            List<BookImage> offererBookImages = bookImageService.getImageByBookId(offererBook.getBookId());


            List<Author> requesterBookAuthor = bookAuthorService.getAuthorsByBookId(requesterBookModel.getBookModelId());
            List<Author> offererBookAuthor = bookAuthorService.getAuthorsByBookId(offererBookModel.getBookModelId());

            List<String> requesterAuthorNames = requesterBookAuthor.stream()
                    .map(Author::getAuthorName)
                    .collect(Collectors.toList());

            List<String> offererAuthorNames = offererBookAuthor.stream()
                    .map(Author::getAuthorName)
                    .collect(Collectors.toList());

            toReturn.add(new ExchangeWrapper(ex, requesterLocation, requesterMail, requesterUsername, offererBook, requesterBook, offererBookModel, requesterBookModel, requesterBookImages, offererBookImages, requesterAuthorNames, offererAuthorNames));
        }

        return toReturn;
    }
}
