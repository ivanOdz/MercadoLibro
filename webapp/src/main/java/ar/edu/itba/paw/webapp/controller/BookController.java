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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class BookController {


    private final BookService bookService;

    private final UserService userService;

    private final BookModelService bookModelService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private BookStateService bookStateService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private BookDimensionService bookDimensionService;

    public BookController(BookService bookService, BookModelService bookModelService) {
        this.bookService = bookService;
        this.bookModelService = bookModelService;
    }


    @RequestMapping("/book")
    public ModelAndView bookHome(@RequestParam(name = "search", defaultValue = "") String search,
                                 @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") boolean isBookStateFilterActive,
                                 @RequestParam(name = "book-state-filter", required = false) BookState bookStateFilter,
                                 @RequestParam(name = "is-genre-filter-active", defaultValue = "false") boolean isGenreFilterActive,
                                 @RequestParam(name = "genre-filter", required = false) Genre genreFilter,
                                 @RequestParam(name = "page-index", defaultValue = "0") int pageIndex,
                                 @RequestParam(name = "sort-type", defaultValue = "BOOK_NAME_ASCENDING") SortType sortType) {


        ModelAndView mav = new ModelAndView("book/book_home");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {

            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);

            List<Book> books =  bookService.getFilteredSortedOrderedBooksByPageFromUser(search, isBookStateFilterActive,
                    bookStateFilter, isGenreFilterActive, genreFilter, pageIndex, pud.getUser().getUserId(), sortType);

            mav.addObject("books", books);
            mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
            mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));
            mav.addObject("bookStateFilter", bookStateFilter);
            mav.addObject("isGenreFilterActive", isGenreFilterActive);
            mav.addObject("genreFilter", genreFilter);
            mav.addObject("isBookStateFilterActive", isBookStateFilterActive);
        }

        return mav;
    }

    @RequestMapping("/book/book_models")
    public ModelAndView bookModels(@RequestParam(name = "search", defaultValue = "") String search,
                                   @RequestParam(name = "is-genre-filter-active", defaultValue = "false") boolean isGenreFilterActive,
                                   @RequestParam(name = "genre-filter", required = false) Genre genreFilter,
                                   @RequestParam(name = "page-index", defaultValue = "0") int pageIndex,
                                   @RequestParam(name = "sort-type", defaultValue = "BOOK_NAME_ASCENDING") SortType sortType) {

        ModelAndView mav = new ModelAndView("book/book_models");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

        List<BookModel> modelBooks = bookModelService.getFilteredSortedOrderedModelBooksByPage(search, isGenreFilterActive, genreFilter, pageIndex, sortType);

        mav.addObject("modelBooks", modelBooks);
        mav.addObject("isGenreFilterActive", isGenreFilterActive);
        mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
        mav.addObject("genreFilter", genreFilter);

        return mav;
    }

    @GetMapping("/book/form_step1")
    public ModelAndView bookModelForm(@ModelAttribute("bookForm") BookForm bookForm, BindingResult errors) {

        ModelAndView mav = new ModelAndView("book/book_form");

        mav.addObject("bookForm", bookForm);
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
        mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));
        mav.addObject("step", 1);

        return mav;
    }

    @PostMapping("/book/create_new_book")
    public ModelAndView createNewBook(@Valid @ModelAttribute("bookForm") BookForm bookForm, BindingResult errors) {
        //if(errors.hasErrors()){
        //    return bookModelForm(bookForm, errors);
        //}
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            user = pud.getUser();
        }

        bookService.createBook(bookForm.getIsbn(), bookForm.getTitle(), bookForm.getAuthors(), bookForm.getEditorial(), bookForm.getDescription(), bookForm.getGenre(), bookForm.getBookState(), bookForm.getEdition(), bookForm.getRating(), bookForm.getImageFiles(), bookForm.getPublicationYear(), bookForm.isHardcover(), bookForm.isPocketEdition(), bookForm.getDimension(), bookForm.getLanguage(), bookForm.getPages(), bookForm.getWeight(), bookForm.getBookCover(), bookForm.isPublish(), user, null);

        return new ModelAndView("redirect:/book");
    }

    @GetMapping("/book/form_step2")
    public ModelAndView bookDetailsFormNewBook(@ModelAttribute(name = "bookForm") BookForm bookForm, @RequestParam(required = false, name = "book_model_id") long bookModelId, BindingResult errors) {

        ModelAndView mav = new ModelAndView("book/book_form");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

        mav.addObject("bookForm", bookForm);
        mav.addObject("step", 2);
        mav.addObject("book_model", bookModelService.getBookModelByBookModelId(bookModelId));
        mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));

        return mav;
    }

    @PostMapping("/book/create_book")
    public ModelAndView createBook(@Valid @ModelAttribute("bookForm") BookForm bookForm, @RequestParam(required = false, name = "book_model_id") long bookModelId, BindingResult errors) {
        if(errors.hasErrors()){
            return bookDetailsFormNewBook(bookForm, bookModelId, errors);
        }
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            user = pud.getUser();
        }
        bookService.createBook(null, null, null, null, null, null, bookForm.getBookState(), 0, bookForm.getRating(), bookForm.getImageFiles(), null, false, false, null, null, 0, 0, 0, bookForm.isPublish(), user, bookModelId);

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
