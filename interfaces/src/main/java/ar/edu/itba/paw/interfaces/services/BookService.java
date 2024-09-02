package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BookService {

    Book getBookById(long publicationId);

}
