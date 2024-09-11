package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.CardBook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardBookServiceImpl implements CardBookService {

    private final BookService bookService;
    private final ImageService imageService;
    private final BookAuthorService bookAuthorService;
    private final BookModelService bookModelService;

    public CardBookServiceImpl(BookService bookService, ImageService imageService, BookAuthorService bookAuthorService, BookModelService bookModelService) {
        this.bookService = bookService;
        this.imageService = imageService;
        this.bookAuthorService = bookAuthorService;
        this.bookModelService = bookModelService;
    }


    @Override
    public List<CardBook> buildCardBookList(List<Book> bookList) {
        List<CardBook> cardBookList = new ArrayList<>();
        for (Book book : bookList) {
            BookModel bookModel = bookModelService.getBookModelByBookModelId(book.getBookModelId());
            Image image = imageService.getFirstImageByBookId(book.getBookId());
            List<Author> authors = bookAuthorService.getAuthorsByBookId(book.getBookId());

            cardBookList.add(new CardBook(book, bookModel, image, authors));
        }
        return cardBookList;
    }
}
