package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.UserNotUnauthorizedException;
import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.webapp.form.BookDetailsForm;
import ar.edu.itba.paw.webapp.form.BookForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Year;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
public class BookController {


    private final BookService bookService;

    private final BookModelService bookModelService;

    private final PublicationService publicationService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private BookStateService bookStateService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private BookDimensionService bookDimensionService;

    @Autowired
    private LoggedUserAdvice loggedUserAdvice;

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;


    public BookController(BookService bookService, BookModelService bookModelService, PublicationService publicationService) {
        this.bookService = bookService;
        this.bookModelService = bookModelService;
        this.publicationService = publicationService;
    }


    @RequestMapping("/book")
    public ModelAndView bookHome(@RequestParam(name = "search", defaultValue = "") String search,
                                 @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") boolean isBookStateFilterActive,
                                 @RequestParam(name = "book-state-filter", required = false) BookState bookStateFilter,
                                 @RequestParam(name = "is-genre-filter-active", defaultValue = "false") boolean isGenreFilterActive,
                                 @RequestParam(name = "genre-filter", required = false) Genre genreFilter,
                                 @RequestParam(name = "page", defaultValue = "0") int currentPage,
                                 @RequestParam(name = "sort-type", defaultValue = "BOOK_NAME_ASCENDING") SortType sortType) {


        User loggeduser = loggedUserAdvice.getLoggedUser();
        
        if (loggeduser == null) {
            String message = messageSource.getMessage("error.unauthorized", null, LocaleContextHolder.getLocale());
            throw new UserNotUnauthorizedException(message);
        }

        ModelAndView mav = new ModelAndView("book/book_home");
        PaginatedResponse<Book, ItemFilterMetadata> books = bookService.getPaginatedBooks(search, isBookStateFilterActive,
                bookStateFilter, isGenreFilterActive, genreFilter, currentPage, loggedUserAdvice.getLoggedUser().getUserId(), sortType);
        
        mav.addObject("books", books);
        mav.addObject("user", loggeduser);
        
        return mav;
    }

    @RequestMapping("/book/book_models")
    public ModelAndView bookModels(@RequestParam(name = "search", defaultValue = "") String search,
                                   @RequestParam(name = "is-genre-filter-active", defaultValue = "false") boolean isGenreFilterActive,
                                   @RequestParam(name = "genre-filter", required = false) Genre genreFilter,
                                   @RequestParam(name = "page", defaultValue = "0") int currentPage,
                                   @RequestParam(name = "sort-type", defaultValue = "BOOK_NAME_ASCENDING") SortType sortType) {

        ModelAndView mav = new ModelAndView("book/book_models");
        PaginatedResponse<BookModel, BookModelMetadata> modelBooks = bookModelService.getPaginatedBookModels(search, isGenreFilterActive, genreFilter, currentPage, sortType);

        mav.addObject("modelBooks", modelBooks);
        mav.addObject("genres", Stream.of(Genre.values()).map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));

        return mav;
    }

    @GetMapping("/book/new_book")
    public ModelAndView bookModelForm(@ModelAttribute("bookForm") BookForm bookForm, BindingResult errors) {

        ModelAndView mav = new ModelAndView("/book/new_book_form");

        mav.addObject("bookForm", bookForm);

        mav.addObject("genres", Stream.of(Genre.values()).map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
        mav.addObject("languages", Stream.of(Language.values()).map(language -> new LanguageWrapper(language, languageService.getLanguageDisplayName(language))).collect(Collectors.toList()));
        mav.addObject("dimensions", Stream.of(BookDimension.values()).map(dimension -> new BookDimensionWrapper(dimension, bookDimensionService.getDimensionDisplayName(dimension))).collect(Collectors.toList()));
        mav.addObject("currentYear", Year.now().getValue());
        mav.addObject("bookStates", Stream.of(BookState.values()).map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));
        mav.addObject("step", 1);

        return mav;
    }

    @PostMapping("/book/create_new_book")
    public ModelAndView createNewBook(@Valid @ModelAttribute("bookForm") BookForm bookForm, BindingResult errors) {
        if (errors.hasErrors()) {
            return bookModelForm(bookForm, errors);
        }

        User user = loggedUserAdvice.getLoggedUser();
        Book book;


//        try {

            book = bookService.createNewBook(bookForm.getIsbn(), bookForm.getTitle(), bookForm.getAuthors(), bookForm.getEditorial(), bookForm.getDescription(), bookForm.getGenre(), bookForm.getEdition(), bookForm.getPublicationYear(), bookForm.isHardcover(), bookForm.isPocketEdition(), bookForm.getDimension(), bookForm.getLanguage(), bookForm.getPages(), bookForm.getWeight(), bookForm.getBookState(), bookForm.getRating(), bookForm.getImageFiles(), bookForm.getBookCover(), user);
        /*} catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }*/

        try {
            publicationService.createPublicationIfNeeded(bookForm.isPublish(), book.getBookId(), user.getUserId(), bookForm.getLocation().getLocationId(), PublicationState.CURRENT);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        return new ModelAndView("redirect:/book");
    }

    @GetMapping("/book/new_book_model")
    public ModelAndView bookDetailsFormNewBook(@ModelAttribute(name = "bookDetailsForm") BookDetailsForm bookDetailsForm, @RequestParam("book_model_id") Long bookModelId, BindingResult errors) {

        ModelAndView mav = new ModelAndView("/book/book_form");

        BookModel bm;
        try {
            bm = bookModelService.getBookModelByBookModelId(bookModelId);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode(), e.getStackTrace());
            return new ModelAndView("redirect:/404");
        }

        mav.addObject("bookDetailsForm", bookDetailsForm);
        mav.addObject("step", 2);
        mav.addObject("book_model", bm);
        mav.addObject("book_model_id", bookModelId);

        mav.addObject("bookStates", Stream.of(BookState.values()).map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));

        return mav;
    }

    // upload from preloaded book model
    @PostMapping("/book/create_book")
    public ModelAndView createBook(@Valid @ModelAttribute(name = "bookDetailsForm") BookDetailsForm bookDetailsForm, BindingResult errors, @RequestParam("book_model_id") Long bookModelId) {
        if (errors.hasErrors()) {
            return bookDetailsFormNewBook(bookDetailsForm, bookModelId, errors);
        }
        User user = loggedUserAdvice.getLoggedUser();

        Book book;

        try {
            book = bookService.createBook(bookModelId, bookDetailsForm.getBookState(), bookDetailsForm.getRating(), bookDetailsForm.getImageFiles(), bookDetailsForm.getBookCover(), null, user, false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ModelAndView("redirect:/400");
        }
        try {
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        try {
            publicationService.createPublicationIfNeeded(bookDetailsForm.isPublish(), book.getBookId(), user.getUserId(), bookDetailsForm.getLocationId(), PublicationState.CURRENT);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        return new ModelAndView("redirect:/book");
    }

}
