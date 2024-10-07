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
        if(loggeduser == null) {
            String message = messageSource.getMessage("error.unauthorized", null, LocaleContextHolder.getLocale());
            throw new UserNotUnauthorizedException(message);
        }

        ModelAndView mav = new ModelAndView("book/book_home");
        PaginatedResponse<Book, ItemFilterMetadata> books = bookService.getPaginatedBooks(search, isBookStateFilterActive,
                bookStateFilter, isGenreFilterActive, genreFilter, currentPage, loggedUserAdvice.getLoggedUser().getUserId(), sortType);
        mav.addObject("books", books);

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
        Number bookId;

        try {
            bookId = bookService.createBook(bookForm.getIsbn(), bookForm.getTitle(), bookForm.getAuthors(), bookForm.getEditorial(), bookForm.getDescription(), bookForm.getGenre(), bookForm.getBookState(), bookForm.getEdition(), bookForm.getRating(), bookForm.getImageFiles(), bookForm.getPublicationYear(), bookForm.isHardcover(), bookForm.isPocketEdition(), bookForm.getDimension(), bookForm.getLanguage(), bookForm.getPages(), bookForm.getWeight(), bookForm.getBookCover(), bookForm.isPublish(), user, null);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        try {
            publicationService.createPublicationIfNeeded(bookForm.isPublish(), bookId.longValue(), user.getUserId(), bookForm.getLocation(), PublicationState.CURRENT);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        return new ModelAndView("redirect:/book");
    }

    @GetMapping("/book/new_book_model")
    public ModelAndView bookDetailsFormNewBook(@ModelAttribute(name = "bookDetailsForm") BookDetailsForm bookDetailsForm, @RequestParam("book_model_id") long bookModelId, BindingResult errors) {

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

    @PostMapping("/book/create_book")
    public ModelAndView createBook(@Valid @ModelAttribute(name = "bookDetailsForm") BookDetailsForm bookDetailsForm, BindingResult errors, @RequestParam("book_model_id") long bookModelId) {
        if (errors.hasErrors()) {
            return bookDetailsFormNewBook(bookDetailsForm, bookModelId, errors);
        }
        User user = loggedUserAdvice.getLoggedUser();

        // FIXME

        Number bookId;
        try{
            bookId = bookService.createBook(null, null, null, null, null, null, bookDetailsForm.getBookState(), 0, bookDetailsForm.getRating(), bookDetailsForm.getImageFiles(), null, false, false, null, null, 0, 0, 0, bookDetailsForm.isPublish(), user, bookModelId);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        publicationService.createPublicationIfNeeded(bookDetailsForm.isPublish(), bookId.longValue(), user.getUserId(), bookDetailsForm.getLocation(), PublicationState.CURRENT);

        return new ModelAndView("redirect:/book");
    }

    /*@GetMapping("/book/book_form")
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
    }*/

    /*@PostMapping("/book/upload_book_model")
    public ModelAndView uploadBookModel(@ModelAttribute(name = "bookForm") BookForm bookForm, BindingResult errors) {
        if(errors.hasErrors()){
            return bookModelForm(bookForm, errors);
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

        final ModelAndView mav = new ModelAndView("redirect:/book/upload_book");
        mav.addObject("bookDetailsForm", bookDetailsForm);
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

        ModelAndView mav = new ModelAndView("redirect:/book/form_step3");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            Book book = bookService.createBook(bookModelId, pud.getUser().getUserId(), bookDetailsForm.getBookState(), 0, bookDetailsForm.getRating());
            if(bookDetailsForm.getImageFiles() != null) {
                List<Image> images = imageService.saveImage(bookDetailsForm.getImageFiles());
                bookImageService.saveBookImage(book.getBookId(), images, new Timestamp(System.currentTimeMillis()));
            } else {
                return new ModelAndView("redirect:/book");
            }
            mav.addObject("book_id", book.getBookId());
        }

        return mav;
    }

    @PostMapping("/book/set_book_cover")
    public ModelAndView setBookCover(@RequestParam(name = "book_id") long bookId, @RequestParam(name = "image_id") long imageId, BindingResult errors) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            bookImageService.setBookCover(bookId, imageId);
        }

        return new ModelAndView("redirect:/book");
    }*/
}
