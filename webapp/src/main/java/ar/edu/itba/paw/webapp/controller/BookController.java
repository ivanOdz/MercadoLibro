package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Card;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import ar.edu.itba.paw.webapp.form.BookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private final CardBookService cardBookService;

    private final UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private BookStateService bookStateService;

    public BookController(SinglePublicationService ps, ImageService imageService, EmailService emailService, ExchangeService exchangeService, PublicationService publicationService, BookService bookService, CardBookService cardBookService, UserService userService) {
        this.ps = ps;
        this.imageService = imageService;
        this.emailService = emailService;
        this.exchangeService = exchangeService;
        this.publicationService = publicationService;
        this.bookService = bookService;
        this.cardBookService = cardBookService;
        this.userService = userService;
    }

    @RequestMapping("/book")
    public ModelAndView bookHome(@RequestParam(name = "search", defaultValue = "") String search) {
        // Obtener todos los libros que tiene el usuario, armar List<Cards>
        ModelAndView mav = new ModelAndView("book/book_home");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {

            List<CardBook> cardBookList = cardBookService.buildCardBookList(bookService.getAllBooksByOwnerIdAndFilteredBy(pud.getUser().getUserId(), search));
            mav.addObject("cardBookList", cardBookList);
            mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
            mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));

        }

        return mav;
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
