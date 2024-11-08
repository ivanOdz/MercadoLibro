package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.BookModelNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.GenreService;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_GENRE_FILTER;
import static ar.edu.itba.paw.models.utils.Constants.DEFAULT_PUBLICATION_STATE_FILTER;

@Service
public class BookModelServiceImpl implements BookModelService {

    @Autowired
    private BookModelDao bookModelDao;

    @Autowired
    private GenreService genreService;

    @Override
    @Transactional
    public BookModel createBookModel(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, Image image) {
        List<Author> newauthors = bookModelDao.createAuthors(authors);
        return bookModelDao.createBookModel(isbn, title, publisher, description, genre, edition, publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight, image, newauthors);
    }

    @Override
    @Transactional(readOnly = true)
    public BookModel getBookModelByBookModelId(Long bookModelId) {
        Optional<BookModel> bookModel = bookModelDao.getBookModelByBookModelId(bookModelId);
        if(bookModel.isEmpty()){
            throw new BookModelNotFoundException("Book model not found");
        }
        return bookModel.get();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, String isGenreFilterActive, String genreFilter, String currentPage, String sortType) {
        boolean genreFilterActive = "true".equalsIgnoreCase(isGenreFilterActive);

        Genre genre = DEFAULT_PUBLICATION_GENRE_FILTER;
        if(genreFilterActive){
            genre = Genre.fromString(genreFilter);
            if(genre == null){
                genreFilterActive = false;
            }
        }

        return bookModelDao.getPaginatedBookModels(search, genreFilterActive, genre, currentPage, sortType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreWrapper> getGenreWrapperList(String search) {
        return bookModelDao.getGenreQtyByBookModel(search);
    }
}
