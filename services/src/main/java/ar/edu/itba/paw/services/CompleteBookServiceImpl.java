package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.CompleteBookService;
import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompleteBookServiceImpl implements CompleteBookService {

    private final BookModelService bookModelService;
    private final BookService bookService;
    private final PublicationService publicationService;

    public CompleteBookServiceImpl(BookModelService bookModelService, BookService bookService, PublicationService publicationService) {
        this.bookModelService = bookModelService;
        this.bookService = bookService;
        this.publicationService = publicationService;
    }



    @Override
    public List<CompleteBook> getCompleteAvailableBooksByUserId(long userId) {
        List<CompleteBook> toReturn = new ArrayList<>();

        List<Book> bookList = bookService.getAllBooksByOwnerIdAndFilteredBy(userId, "");

        for (Book book : bookList) {
            BookModel bookModel = bookModelService.getBookModelByBookModelId(book.getBookModelId());
            Optional<Publication> publication = publicationService.getPublicationStateByBookId(book.getBookId());

            if(publication.isEmpty() || publication.get().getPublicationState() == PublicationState.TERMINATED) {
                toReturn.add(new CompleteBook(book, bookModel));
            }
        }

        return toReturn;
    }
}
