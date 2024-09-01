package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Book;

import java.util.Optional;

public interface BookDao {
    Optional<Book> getBookById(long publicationId);
}
