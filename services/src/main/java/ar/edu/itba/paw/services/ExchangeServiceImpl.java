package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ResponseState;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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


    public ExchangeServiceImpl(final ExchangeDao exchangeDao, BookService bookService, BookModelService bookModelService, ImageService imageService, PublicationService publicationService, BookAuthorService bookAuthorService, BookImageService bookImageService, LocationService locationService, UserService userService){
        this.exchangeDao = exchangeDao;
        this.bookService = bookService;
        this.bookModelService = bookModelService;
        this.imageService = imageService;
        this.publicationService = publicationService;
        this.bookAuthorService = bookAuthorService;
        this.bookImageService = bookImageService;
        this.locationService = locationService;
        this.userService = userService;
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
    public Exchange initializeExchange(boolean isForExchange, long requesterPubId, long offererPubId) {
//        if(isForExchange) {
            Random random = new Random();
            int acceptCode = Math.abs(random.nextInt());
            return exchangeDao.createExchange(offererPubId, requesterPubId, acceptCode);
//        }
//        return null;
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

            toReturn.add(new ExchangeWrapper(ex, requesterLocation, requesterMail, requesterUsername, offererBook, requesterBook, offererBookModel, requesterBookModel, requesterBookImages, offererBookImages, requesterBookAuthor, offererBookAuthor));
        }

        return toReturn;
    }
}
