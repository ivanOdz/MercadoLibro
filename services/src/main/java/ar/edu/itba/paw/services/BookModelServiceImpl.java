package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import org.springframework.stereotype.Service;

@Service
public class BookModelServiceImpl implements BookModelService {

    private final BookModelDao bookModelDao;

    public BookModelServiceImpl(BookModelDao bookModelDao) {
        this.bookModelDao = bookModelDao;
    }


    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        return bookModelDao.getBookModelByBookModelId(bookModelId);
    }

    @Override
    public BookModel addBookModel(String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language language, BookDimension dimension, Short publicationYear, boolean pocketEdition, boolean hardcover) {
        return bookModelDao.addBookModel(isbn, title, editorial, description, genre, edition, weight, pages, language, dimension, publicationYear, pocketEdition, hardcover);
    }
}
