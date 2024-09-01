package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BookService {

    Optional<Book> getBookById(long publicationId);

}
