package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public interface BookService {

    Optional<Book> getBookById(long publicationId);
    Book createBook(String isbn, String title, List<String> author, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, long userId);
    void exchangeOwnership(long b1, long b2);

}
