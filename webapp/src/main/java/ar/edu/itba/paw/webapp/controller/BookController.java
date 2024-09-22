package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import ar.edu.itba.paw.webapp.form.BookDetailsForm;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.ModelBookForm;
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
    public ModelAndView bookHome(@RequestParam(name = "search", defaultValue = "") String search,
                                 @RequestParam(name = "book-state-filter", defaultValue = "6") int bookStateFilter,
                                 @RequestParam(name = "genre-filter", defaultValue = "32") int genreFilter) {

        // Obtener todos los libros que tiene el usuario, armar List<Cards>
        ModelAndView mav = new ModelAndView("book/book_home");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {

            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);

            List<CardBook> cardBookList = cardBookService.buildCardBookList(bookService.getAllBooksByOwnerIdAndFilteredBy(pud.getUser().getUserId(), search, bookStateFilter, genreFilter));
            mav.addObject("cardBookList", cardBookList);
            mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
            mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));

            mav.addObject("bookStateFilter", bookStateFilter);
            mav.addObject("genreFilter", genreFilter);
        }

        return mav;
    }

    @RequestMapping("/book/book_models")
    public ModelAndView bookModels(@RequestParam(name = "search", defaultValue = "") String search,
                                   @RequestParam(name = "genre-filter", defaultValue = "32") int genreFilter) {

        ModelAndView mav = new ModelAndView("book/book_models");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

        List<CardBook> cardBookList = cardBookService.buildCardBookModelList(bookModelService.getAllBookModelFilteredBy(search, genreFilter));
        mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));

        mav.addObject("cardBookList", cardBookList);

        mav.addObject("genreFilter", genreFilter);

        return mav;
    }

    @GetMapping("/book/form_step1")
    public ModelAndView bookModelForm(@ModelAttribute("modelBookForm") ModelBookForm modelBookForm, BindingResult errors) {

        ModelAndView mav = new ModelAndView("book/form/step1");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

        mav.addObject("modelBookForm", modelBookForm);
        mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
        mav.addObject("languages", List.of(Language.values()).stream().map(language -> new LanguageWrapper(language, languageService.getLanguageDisplayName(language))).collect(Collectors.toList()));
        mav.addObject("dimensions", List.of(BookDimension.values()).stream().map(dimension -> new BookDimensionWrapper(dimension, bookDimensionService.getDimensionDisplayName(dimension))).collect(Collectors.toList()));
        mav.addObject("currentYear", Year.now().getValue());

        return mav;
    }

    @GetMapping("/book/form_step2")
    public ModelAndView bookDetailsForm(@ModelAttribute("bookDetailsForm") BookDetailsForm bookDetailsForm,
                                        @RequestParam(name = "book_model_id") long bookModelId, BindingResult errors) {

        ModelAndView mav = new ModelAndView("book/form/step2");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

        mav.addObject("bookDetailsForm", bookDetailsForm);
        mav.addObject("book_model", bookModelService.getBookModelByBookModelId(bookModelId));
        mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));

        return mav;
    }

    @GetMapping("/book/book_form")
    public ModelAndView bookForm(@ModelAttribute("bookForm") BookForm bookForm) {

        final ModelAndView mav = new ModelAndView("book/book_form");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

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

    /*@PostMapping("/book/upload_book")
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

            List<Image> images = imageService.saveImage(bookForm.getImageFiles());
            bookImageService.saveBookImage(book.getBookId(), images, new Timestamp(System.currentTimeMillis()));
        }

        return new ModelAndView("redirect:/book");
    }*/

    @PostMapping("/book/upload_book_model")
    public ModelAndView uploadBookModel(@Valid @ModelAttribute("modelBookForm") ModelBookForm
                                                modelBookForm, BindingResult errors) {
        if (errors.hasErrors()) {
            return bookModelForm(modelBookForm, errors);
        }
        BookModel bookModel = bookModelService.addBookModel(
                modelBookForm.getAuthors(),
                modelBookForm.getIsbn(),
                modelBookForm.getTitle(),
                modelBookForm.getEditorial(),
                modelBookForm.getDescription(),
                modelBookForm.getGenre(),
                modelBookForm.getEdition(),
                modelBookForm.getWeight(),
                modelBookForm.getPages(),
                modelBookForm.getLanguage(),
                modelBookForm.getDimension(),
                modelBookForm.getPublicationYear(),
                modelBookForm.getIsPocketEdition(),
                modelBookForm.getIsHardcover()
        );

        final ModelAndView mav = new ModelAndView("redirect:/book/form_step2?book_model_id=" + bookModel.getBookModelId());
        mav.addObject("book_model_id", bookModel.getBookModelId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

        return mav;
    }

    @PostMapping("/book/upload_book")
    public ModelAndView uploadBook(@Valid @ModelAttribute("bookDetailsForm") BookDetailsForm bookDetailsForm, @RequestParam(name = "book_model_id") long bookModelId, BindingResult errors) {
        if(errors.hasErrors()){
            return bookDetailsForm(bookDetailsForm, bookModelId, errors);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            Book book = bookService.createBook(bookModelId, pud.getUser().getUserId(), bookDetailsForm.getBookState(), 0, bookDetailsForm.getRating());
            if(bookDetailsForm.getImageFiles() != null) {
                List<Image> images = imageService.saveImage(bookDetailsForm.getImageFiles());
                bookImageService.saveBookImage(book.getBookId(), images, new Timestamp(System.currentTimeMillis()));
            }
        }

        return new ModelAndView("redirect:/book");
    }
}
