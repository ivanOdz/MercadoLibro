package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.BookModel;

public interface BookModelDao {
    BookModel getBookModelByBookModelId(long bookModelId);
}
