package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.CardBook;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardBookServiceImpl implements CardBookService {

    private final BookService bookService;
    private final ImageService imageService;
    private final BookAuthorService bookAuthorService;
    private final BookModelService bookModelService;
    private final PublicationService publicationService;

    public CardBookServiceImpl(BookService bookService, ImageService imageService, BookAuthorService bookAuthorService, BookModelService bookModelService, PublicationService publicationService) {
        this.bookService = bookService;
        this.imageService = imageService;
        this.bookAuthorService = bookAuthorService;
        this.bookModelService = bookModelService;
        this.publicationService = publicationService;
    }


    @Override
    public List<CardBook> buildCardBookList(List<Book> bookList) {
        List<CardBook> cardBookList = new ArrayList<>();
        for (Book book : bookList) {
            BookModel bookModel = bookModelService.getBookModelByBookModelId(book.getBookModelId());
            Image image = imageService.getFirstImageByBookId(book.getBookId());

            Long imageId = null;

            if(image != null){
                imageId = image.getImageId();
            }

            List<Author> authors = bookAuthorService.getAuthorsByBookId(book.getBookId());

            boolean canPublish = false;
            Optional<Publication> publication = publicationService.getPublicationStateByBookId(book.getBookId());
            if(publication.isEmpty() || publication.get().getPublicationState() == PublicationState.TERMINATED) {
                canPublish = true;
            }
            cardBookList.add(new CardBook(book, bookModel, imageId, authors, canPublish));
        }
        return cardBookList;
    }

    @Override
    public List<CardBook> buildCardBookModelList(List<BookModel> bookList) {
        List<CardBook> cardBookList = new ArrayList<>();
        for(BookModel bookModel : bookList){
            Image image = imageService.getFirstImageByBookId(bookModel.getBookModelId());

            Long imageId = null;
            if(image != null){
                imageId = image.getImageId();
            }

            List<Author> authors = bookAuthorService.getAuthorsByBookId(bookModel.getBookModelId());

            cardBookList.add(new CardBook(bookModel, imageId, authors));
        }
        return cardBookList;
    }
}
