package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.AuthorService;
import ar.edu.itba.paw.interfaces.services.BookAuthorService;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookModelServiceImpl implements BookModelService {

    private final BookModelDao bookModelDao;

    private final AuthorService authorService;

    private final BookAuthorService bookAuthorService;


    public BookModelServiceImpl(BookModelDao bookModelDao, AuthorService authorService, BookAuthorService bookAuthorService) {
        this.bookModelDao = bookModelDao;
        this.authorService = authorService;
        this.bookAuthorService = bookAuthorService;
    }


    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        return bookModelDao.getBookModelByBookModelId(bookModelId);
    }

    /*@Override
    public BookModel addBookModel(List<String> authors, String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language language, BookDimension dimension, Short publicationYear, boolean pocketEdition, boolean hardcover) {
        BookModel bookModel = bookModelDao.addBookModel(isbn, title, editorial, description, genre, edition, weight, pages, language, dimension, publicationYear, pocketEdition, hardcover);
        for(String author : authors) {
            Author a = authorService.createAuthor(author);
            bookAuthorService.createBookAuthor(bookModel.getBookModelId(),a.getAuthorId());
        }
        return bookModel;
    }*/

    @Override
    public List<BookModel> getBookModelByUserId(long userId) {
        return bookModelDao.getBookModelByUserId(userId);
    }

    @Override
    public List<BookModel> getAllBookModelFilteredBy(String search, int genreFilter) {
        return bookModelDao.getAllBookModel(search, genreFilter);
    }

    @Override
    public Rating getRatingByBookModelId(long bookModelId) {
        return bookModelDao.getRatingByBookModelId(bookModelId);
    }

    @Override
    public List<BookModel> getFilteredSortedOrderedModelBooksByPage(String search, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, SortType sortType) {
        return bookModelDao.getFilteredSortedOrderedModelBooksByPage(search, isGenreFilterActive, genreFilter, pageIndex, sortType);
    }
}
