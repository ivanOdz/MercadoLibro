package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.Rating;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationDetailServiceImpl implements PublicationDetailService {

    PublicationService publicationService;
    BookService bookService;
    BookModelService bookModelService;
    BookImageService bookImageService;

    public PublicationDetailServiceImpl(PublicationService publicationService, BookService bookService, BookModelService bookModelService, BookImageService bookImageService) {
        this.publicationService = publicationService;
        this.bookService = bookService;
        this.bookModelService = bookModelService;
        this.bookImageService = bookImageService;
    }

    @Override
    public PublicationDetail getPublicationDetail(long publicationId) {
        Publication publication = publicationService.getPublicationById(publicationId).orElse(null);
        Book book = bookService.getBookById(publication.getBookId()).orElse(null);
        BookModel bookModel = bookModelService.getBookModelByBookModelId(book.getBookModelId());
        List<BookImage> bookImageList = bookImageService.getImageByBookId(book.getBookId());
        Rating rating = bookModelService.getRatingByBookModelId(bookModel.getBookModelId());

        return new PublicationDetail(bookModel, book, bookImageList, rating);
    }
}
