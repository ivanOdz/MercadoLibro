package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.BookImage;

import java.util.List;

public interface BookImageDao {

    List<BookImage> getImageByBookId(long bookId);

}
