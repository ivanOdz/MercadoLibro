package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.BookAuthor;
import org.springframework.stereotype.Service;

@Service
public interface BookAuthorService {

    BookAuthor createBook_Author(long bookId, long authorId);

}
