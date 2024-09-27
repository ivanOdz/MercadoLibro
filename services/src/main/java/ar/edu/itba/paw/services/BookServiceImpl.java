package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Book;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.models.utils.SortType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    private final BookModelService bookModelService;
    private final ImageService imageService;

    public BookServiceImpl(final BookDao bookDao, BookModelService bookModelService, ImageService imageService) {
        this.bookDao = bookDao;
        this.bookModelService = bookModelService;
        this.imageService = imageService;
    }

    /*
    @Override
    public Optional<Book> getBookById(long publicationId) {
        return bookDao.getBookById(publicationId);
    }

    @Override
    public void exchangeOwnership(long b1, long b2) {
        bookDao.exchangeOwnership(b1, b2);
    }

    @Override
    public Book getBookByPubId(long pubId) {
        return bookDao.getBookByPubId(pubId);
    }

    @Override
    public List<Book> getAllBooksByOwnerIdAndFilteredBy(long ownerId, String search, int bookStateFilter, int genreFilter) {
        return bookDao.getAllBooksByOwnerIdAndFilteredBy(ownerId, search, bookStateFilter, genreFilter);
    }*/

    @Override
    public List<Book> getFilteredSortedOrderedBooksByPageFromUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType) {
        return bookDao.getFilteredSortedOrderedBooksByPageFromUser(search, isBookStateFilterActive, bookStateFilter, isGenreFilterActive, genreFilter, pageIndex, userId, sortType);
    }

    @Override
    public Book createBook () {

        // Si el book model ya existia, solamente inserto el book
        // Caso contrario, inserto primero el bookModel, devuelvo el id, y eso lo tomo para insertar el book

        long bookModelId;
        if(/*BOOK MODEL NO EXISTE*/){
            // Inserto Image mediante el service de Image
            // Inserto los Author, BookAuthor, BookRating mediante el service de book model
            long imageId = imageService.saveImage(/*Portada book model*/).getFirst().getImageId();
            bookModelId = bookModelService.createBookModel();
        }

        // Inserto imagenes del book y recupero los ids.
        List<Long> imagesId = imageService.saveImage(/*lista de MultipartFile del book*/).stream().map(Image::getImageId).toList();

        // Inserto el book y lo retorno. Si Book = null, informar en la vista
        return bookDao.createBook();
    }
}


















