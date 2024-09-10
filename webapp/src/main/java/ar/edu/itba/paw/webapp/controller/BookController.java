package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.webapp.form.BookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class BookController {

    private final SinglePublicationService ps;

    private final ImageService imageService;

    private final EmailService emailService;

    private final ExchangeService exchangeService;

    private final PublicationService publicationService;

    private final BookService bookService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private BookStateService bookStateService;

    public BookController(SinglePublicationService ps, ImageService imageService, EmailService emailService, ExchangeService exchangeService, PublicationService publicationService, BookService bookService, UserService userService) {
        this.ps = ps;
        this.imageService = imageService;
        this.emailService = emailService;
        this.exchangeService = exchangeService;
        this.publicationService = publicationService;
        this.bookService = bookService;
        this.userService = userService;
    }


    private final UserService userService;

    @RequestMapping("/book")
    public ModelAndView bookHome() {
        return new ModelAndView("book/book_home");
    }


    @GetMapping("/book/upload_new_book")
    public ModelAndView uploadNewBook(@ModelAttribute("bookForm") BookForm bookForm) {

        final ModelAndView mav = new ModelAndView("book/newbook");

        if (bookForm.getAuthors() == null) {
            bookForm.setAuthors(new ArrayList<>());
        }

        mav.addObject("bookForm", bookForm);
        mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
        mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));


        return mav;
    }
}
