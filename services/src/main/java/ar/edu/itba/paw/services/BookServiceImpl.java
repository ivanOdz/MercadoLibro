package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Book;

import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
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
    }*/

/*
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
    public void exchangeOwnership(Book b1, Book b2) {
        bookDao.setOwner(b1.getBookId(), b2.getOwner().getUserId());
        bookDao.setOwner(b2.getBookId(), b1.getOwner().getUserId());
    }

    @Override
    public Number createBook(String isbn, String title, List<String> authors, String publisher, String description, Genre genre, BookState bookState, int edition,
                           int rating, List<MultipartFile> imageFiles, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension,
                           Language language, int pages, int weight, int bookCoverIndex, boolean publish, User user, Long bookModelId) {

        // Inserto imagenes del book y recupero los ids.
        List<Integer> imagesId = imageService.saveImage(arrangeImages(imageFiles, bookCoverIndex)).stream().map(Image::getImageId).toList();

        // Si el book model ya existia, tomo el id y solamente inserto el book
        // Caso contrario, inserto primero el bookModel, devuelvo el bookModel, y a eso le tomo el id para insertar luego el book
        Long bmId = bookModelId;
        if(bmId == null) {
            bmId = bookModelService.createBookModel(isbn, title, authors, publisher, description, genre, edition,
                                                            publicationYear, isHardcover, isPocketEdition, dimension, language, pages, weight, imagesId.get(bookCoverIndex));
        }

        // Creo el book_rating
        bookDao.createBookRating(user, bmId, rating);

        // Inserto el book
        Number toReturn = bookDao.createBook(bmId, user, bookState, imagesId);

        // Con los ids de las imagenes y el bookId, creo los book_images
        bookDao.createBookImage(toReturn.longValue(), imagesId);

        return toReturn;
    }

    public List<MultipartFile> arrangeImages(List<MultipartFile> images, int bookCoverIndex) {
        if(bookCoverIndex == 0){
            return images;
        }
        List<MultipartFile> toReturn = new ArrayList<>();
        toReturn.add(images.get(bookCoverIndex));
        for (MultipartFile image : images) {
            if(images.indexOf(image) != bookCoverIndex){
                toReturn.add(image);
            }
        }
        return toReturn;
    }
}


















