package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookAuthor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookAuthorService {

    BookAuthor createBook_Author(long bookId, long authorId, List<BookAuthor> bookAuthors);

    List<Author> getAuthorsByBookId(long bookId);


}
