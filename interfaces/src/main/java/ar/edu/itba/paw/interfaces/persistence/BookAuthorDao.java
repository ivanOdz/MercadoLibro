package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.BookAuthor;

public interface BookAuthorDao {

    BookAuthor createBook_Author(long bookId, long authorId);

}
