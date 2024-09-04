package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.CardService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.PublicationsService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Card;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Publication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final BookService bookService;
    private final ImageService imageService;

    public CardServiceImpl(BookService bookService, ImageService imageService) {
        this.bookService = bookService;
        this.imageService = imageService;
    }

    @Override
    public Card createCard(Publication publication, Book book, Image image) {
        return new Card(publication, book, image);
    }

    @Override
    public List<Card> buildCardList(List<Publication> publicationsList) {
        List<Card> cardList = new ArrayList<>();
        for (Publication publication : publicationsList) {
            Book book = bookService.getBookById(publication.getBookId()).get();
            Image image = imageService.getImageById(book.getImage()).orElse(null);
            cardList.add(new Card(publication, book, image));
        }
        return cardList;
    }


}
