package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final BookService bookService;
    private final BookAuthorService bookAuthorService;
    private final ImageService imageService;
    private final PublicationService publicationsService;
    private final BookModelService bookModelService;
    private final BookImageService bookImageService;
    private final LocationService locationService;

    public CardServiceImpl(BookService bookService, BookAuthorService bookAuthorService, ImageService imageService, PublicationService publicationsService, BookModelService bookModelService, BookImageService bookImageService, LocationService locationService) {
        this.bookService = bookService;
        this.bookAuthorService = bookAuthorService;
        this.imageService = imageService;
        this.publicationsService = publicationsService;
        this.bookModelService = bookModelService;
        this.bookImageService = bookImageService;
        this.locationService = locationService;
    }

    @Override
    public Card createCard(Publication publication, Book book, BookModel bookModel, List<Image> images, List<Author> authors, String location) {
        return new Card(publication, book, bookModel, images, authors, location);
    }

    @Override
    public Card createCard(long publicationId) {
        Publication publication = publicationsService.getPublicationById(publicationId).get();
        Book book = bookService.getBookById(publication.getBookId()).get();
        BookModel bookModel = bookModelService.getBookModelByBookModelId(book.getBookModelId());
        List<BookImage> bookImages = bookImageService.getSortedImagesByBookId(bookModel.getBookModelId());
        List<Image> images = imageService.getImagesByBookImageList(bookImages);
        List<Author> authors = bookAuthorService.getAuthorsByBookId(book.getBookId());
        String location = locationService.getLocationByPublicationId(publicationId);
        return new Card(publication, book, bookModel, images, authors, location);
    }

    @Override
    public List<Card> buildCardList(List<Publication> publicationsList) {
        List<Card> cardList = new ArrayList<>();
        for (Publication publication : publicationsList) {
            Book book = bookService.getBookById(publication.getBookId()).get();
//            Image image = imageService.getImageById(book.get()).orElse(null);
            List<Author> authors = bookAuthorService.getAuthorsByBookId(book.getBookId());
            BookModel bookModel = bookModelService.getBookModelByBookModelId(book.getBookModelId());
            cardList.add(new Card(publication, book, bookModel, /*image*/null, authors, "Argentina"));
        }
        return cardList;
    }




}
