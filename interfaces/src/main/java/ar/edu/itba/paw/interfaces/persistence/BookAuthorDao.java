package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookAuthor;

import java.util.List;

public interface BookAuthorDao {

    BookAuthor createBookAuthor(long bookModelId, long authorId);

    List<Author> getAuthorsByBookId(long bookId);

}
