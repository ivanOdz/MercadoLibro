package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Card;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Publication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {
    Card createCard(Publication publication, Book book, Image image);
    List<Card> buildCardList(List<Publication> publicationsList);
}
