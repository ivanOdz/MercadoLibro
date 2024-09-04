package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.*;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SinglePublicationServiceImpl implements SinglePublicationService {

    private final SinglePublicationDao publicationDao;
    private final UserService userService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final BookAuthorService book_authorService;

    public SinglePublicationServiceImpl(SinglePublicationDao publicationDao, UserService userService, BookService bookService, AuthorService authorService, BookAuthorService book_authorService) {
        this.publicationDao = publicationDao;
        this.userService = userService;
        this.bookService = bookService;
        this.authorService = authorService;
        this.book_authorService = book_authorService;
    }

    @Override
    public Publication createPublication(String username, String mail, String isbn, String title, List<String> authors, String editorial, String description, Genres genre, BookState bookState, PublicationState publicationState, int edition, int rating, long image, String location) {
        
    	User user = userService.createUser(username, mail);
        Book book = bookService.createBook(isbn, title, editorial, description, genre, bookState, publicationState, edition, rating, image, user.getId());

        for (String author : authors) {
            System.out.println("autor: " + author);
            Author auth = authorService.createAuthor(author);
            book_authorService.createBook_Author(book.getBookId(), auth.getAuthorId());

        }
        return publicationDao.createPublication(book.getBookId(), user.getId(), location);
    }
}
