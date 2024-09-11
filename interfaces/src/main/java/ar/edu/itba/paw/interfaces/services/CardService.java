package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {
    Card createCard(Publication publication, Book book, BookModel bookModel, List<Image> images, List<Author> bookAuthors, String location);
    public Card createCard(long publicationId);
    List<Card> buildCardList(List<Publication> publicationsList);
}
