package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.CardBook;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import ar.edu.itba.paw.webapp.form.BookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Year;
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

    private final BookModelService bookModelService;

    private final BookImageService bookImageService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private BookStateService bookStateService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private BookDimensionService bookDimensionService;


    public BookController(SinglePublicationService ps, ImageService imageService, EmailService emailService, ExchangeService exchangeService, PublicationService publicationService, BookService bookService, BookModelService bookModelService, CardBookService cardBookService, UserService userService, BookImageService bookImageService) {
        this.ps = ps;
        this.imageService = imageService;
        this.emailService = emailService;
        this.exchangeService = exchangeService;
        this.publicationService = publicationService;
        this.bookService = bookService;
        this.bookModelService = bookModelService;
        this.cardBookService = cardBookService;
        this.userService = userService;
        this.bookImageService = bookImageService;
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


    @GetMapping("/book/book_form")
    public ModelAndView bookForm(@ModelAttribute("bookForm") BookForm bookForm) {

        final ModelAndView mav = new ModelAndView("book/book_form");

        if (bookForm.getAuthors() == null) {
            bookForm.setAuthors(new ArrayList<>());
        }

        mav.addObject("bookForm", bookForm);
        mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
        mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));
        mav.addObject("languages", List.of(Language.values()).stream().map(language -> new LanguageWrapper(language, languageService.getLanguageDisplayName(language))).collect(Collectors.toList()));
        mav.addObject("dimensions", List.of(BookDimension.values()).stream().map(dimension -> new BookDimensionWrapper(dimension, bookDimensionService.getDimensionDisplayName(dimension))).collect(Collectors.toList()));
        mav.addObject("currentYear", Year.now().getValue());
        return mav;
    }

    @PostMapping("/book/upload_book")
    public ModelAndView uploadBook(@Valid @ModelAttribute("bookForm") BookForm bookForm, BindingResult errors) {
        if(errors.hasErrors()){
            return bookForm(bookForm);
        }

        BookModel bookModel = bookModelService.addBookModel(
                bookForm.getAuthors(),
                bookForm.getIsbn(),
                bookForm.getTitle(),
                bookForm.getEditorial(),
                bookForm.getDescription(),
                bookForm.getGenre(),
                bookForm.getEdition(),
                bookForm.getWeight(),
                bookForm.getPages(),
                bookForm.getLanguage(),
                bookForm.getDimension(),
                bookForm.getPublicationYear(),
                bookForm.getIsPocketEdition(),
                bookForm.getIsHardcover()
                );


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            Book book = bookService.createBook(bookModel.getBookModelId(), pud.getUser().getUserId(), bookForm.getBookState(), 0,bookForm.getRating());

            System.out.println("La lista de libros viene como: "+ bookForm.getImageFiles());
            List<Image> images = imageService.saveImage(bookForm.getImageFiles());
            bookImageService.saveBookImage(book.getBookId(), images, new Timestamp(System.currentTimeMillis()));
        }

        return new ModelAndView("redirect:/book");
    }
}
