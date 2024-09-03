package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book createBook(String isbn, String title, List<String> authors, String editorial, String description, Genres genre, BookState bookState, PublicationState publicationState, int edition, int rating, long image, long userId);

    Optional<Book> getBookById(long publicationId);
    void exchangeOwnership(long b1, long b2);
}
