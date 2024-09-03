package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book createBook(String isbn, String title, List<String> authors, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, long userId);

    Optional<Book> getBookById(long publicationId);
}
