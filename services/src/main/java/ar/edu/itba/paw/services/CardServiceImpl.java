package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    private final BookService bookService;
    private final ImageService imageService;
    private final BookAuthorService bookAuthorService;

    public CardServiceImpl(BookService bookService, ImageService imageService, BookAuthorService bookAuthorService) {
        this.bookService = bookService;
        this.imageService = imageService;
        this.bookAuthorService = bookAuthorService;
    }

    @Override
    public Card createCard(Publication publication, Book book, Image image, List<Author> authors) {
        return new Card(publication, book, image, authors);
    }

    @Override
    public List<Card> buildCardList(List<Publication> publicationsList) {
        List<Card> cardList = new ArrayList<>();
        for (Publication publication : publicationsList) {
            Book book = bookService.getBookById(publication.getBookId()).get();
            //Image image = imageService.getImageById(book.get()).orElse(null);
            List<Author> authors = bookAuthorService.getAuthorsByBookId(book.getBookId());
            cardList.add(new Card(publication, book, /*image*/null, authors));
        }
        return cardList;
    }


}
