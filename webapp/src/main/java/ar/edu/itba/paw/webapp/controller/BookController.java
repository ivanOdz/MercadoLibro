package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.webapp.dto.Book.BookDTO;
import ar.edu.itba.paw.webapp.dto.Book.BookModelDTO;
import ar.edu.itba.paw.webapp.form.BookDetailsForm;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;


@Path("/books")
@Component
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookModelService bookModelService;

    @Autowired
    private PublicationService publicationService;

    @Context
    private UriInfo uriInfo;

    /*@RequestMapping("/book")
    public ModelAndView bookHome(@RequestParam(name = "search", defaultValue = "") String search,
                                 @RequestParam(name = "is-book-state-filter-active", defaultValue = "false") String isBookStateFilterActive,
                                 @RequestParam(name = "book-state-filter", required = false) String bookStateFilter,
                                 @RequestParam(name = "is-genre-filter-active", defaultValue = "false") String isGenreFilterActive,
                                 @RequestParam(name = "genre-filter", required = false) String genreFilter,
                                 @RequestParam(name = "page", defaultValue = "0") int currentPage,
                                 @RequestParam(name = "sort-type", defaultValue = "BOOK_NAME_ASCENDING") String sortType,
                                 @ModelAttribute("loggedUser") User loggeduser) {

        ModelAndView mav = new ModelAndView("book/book_home");


        List<GenreWrapper> genreWrapperList = bookService.getGenreWrapperList(search, isBookStateFilterActive, bookStateFilter, loggeduser.getUserId());
        List<BookStateWrapper> bookStateWrapperList = bookService.getBookStateWrapperList(search, isGenreFilterActive, genreFilter, loggeduser.getUserId());

        List<Publication> activePublications = publicationService.getActivePublicationsByUser(loggeduser);

        mav.addObject("books", books);
        mav.addObject("genreWrapperList", genreWrapperList);
        mav.addObject("bookStateWrapperList", bookStateWrapperList);
        mav.addObject("bookStates", BookState.values());

        mav.addObject("activePublications", activePublications);
        mav.addObject("publicationForm", new PublicationForm());

        return mav;
    }
    */

    @GET
    @Produces(value = {VndType.APPLICATION_BOOKS})
    public Response getBooks(@QueryParam("search") @DefaultValue("")final String search,
                             @QueryParam("sort-type") @DefaultValue("BOOK_NAME_ASCENDING") final String sortType,
                             @QueryParam("is-book-state-filter-active") @DefaultValue("false") final String isBookStateFilterActive,
                             @QueryParam("book-state-filter") String bookStateFilter, //required = false
                             @QueryParam("genre-filter") final String genreFilter,  // required = false
                             @QueryParam("page") @DefaultValue("0")final int currentPage,
                             @QueryParam("is-genre-filter-active") @DefaultValue("false") final String isGenreFilterActive,
                             @ModelAttribute("loggedUser") User loggeduser) {
        PaginatedResponse<Book, ItemFilterMetadata> paginated = bookService.getPaginatedBooks(search, isBookStateFilterActive,
                bookStateFilter, isGenreFilterActive, genreFilter, currentPage, loggeduser.getUserId(), sortType);
        final List<BookDTO> books = paginated.getData().stream()
                .map(book -> BookDTO.fromBook(uriInfo, book)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<BookDTO>>(books) {}).build();
    }

    /*
    // ASK because it should be a /books/book_model but could be NOT RESTful
    @RequestMapping("/book/book_models")
    public ModelAndView bookModels(@RequestParam(name = "search", defaultValue = "") String search,
                                   @RequestParam(name = "is-genre-filter-active", defaultValue = "false") String isGenreFilterActive,
                                   @RequestParam(name = "genre-filter", required = false) String genreFilter,
                                   @RequestParam(name = "page", defaultValue = "0") int currentPage,
                                   @RequestParam(name = "sort-type", defaultValue = "BOOK_NAME_ASCENDING") String sortType) {

        ModelAndView mav = new ModelAndView("book/book_models");
        PaginatedResponse<BookModel, BookModelMetadata> modelBooks = bookModelService.getPaginatedBookModels(search, isGenreFilterActive, genreFilter, currentPage, sortType);

        List<GenreWrapper> genreWrapperList = bookModelService.getGenreWrapperList(search);

        mav.addObject("genres", genreWrapperList);
        mav.addObject("modelBooks", modelBooks);

        return mav;
    }*/

    @GET
    @Path("/book_models")
    @Produces(value = {VndType.APPLICATION_BOOK_MODELS})
    public Response getBookModels(@QueryParam("search") @DefaultValue("")final String search,
                                  @QueryParam("is-genre-filter-active") @DefaultValue("false")final String isGenreFilterActive,
                                  @QueryParam("genre-filter")final String genreFilter,
                                  @QueryParam("page") @DefaultValue("0") final int currentPage,
                                  @QueryParam("sort-type") @DefaultValue("BOOK_NAME_ASCENDING") String sortType) {

        PaginatedResponse<BookModel, BookModelMetadata> modelBooks = bookModelService.getPaginatedBookModels(search, isGenreFilterActive, genreFilter, currentPage, sortType);

        List<BookModelDTO> bookModels = modelBooks.getData().stream().map(bm -> BookModelDTO.fromBookModel(uriInfo, bm)).toList();

        return Response.ok(new GenericEntity<List<BookModelDTO>>(bookModels) {}).build();
    }

    @PostMapping("/book/create_book")
    public ModelAndView createBook(@Valid @ModelAttribute(name = "bookDetailsForm") BookDetailsForm bookDetailsForm, BindingResult errors, @RequestParam("book_model_id") Long bookModelId, @ModelAttribute("loggedUser") User loggeduser) {
        if (errors.hasErrors()) {
            return bookDetailsFormNewBook(bookDetailsForm, bookModelId, errors, loggeduser);
        }
        Book book = bookService.createBook(bookModelId, bookDetailsForm.getBookState(), bookDetailsForm.getRating(), bookDetailsForm.getImageFiles(), bookDetailsForm.getBookCover(), null, loggeduser, false);

        publicationService.createPublicationIfNeeded(bookDetailsForm.isPublish(), book.getBookId(), loggeduser.getUserId(), bookDetailsForm.getLocationId(), PublicationState.CURRENT);
        return new ModelAndView("redirect:/book");
    }

    @PostMapping("/update_bookstate")
    public ModelAndView updateBookState(@RequestParam("book_id") Long bookId, @RequestParam("book_state") String bookState) {
        bookService.updateBookState(bookId, bookState);

        return new ModelAndView("redirect:/book");
    }


    @PostMapping("/book/create_new_book")
    public ModelAndView createNewBook(@Valid @ModelAttribute("bookForm") BookForm bookForm, BindingResult errors, @ModelAttribute("loggedUser") User loggeduser) {
        if (errors.hasErrors()) {
            return bookModelForm(bookForm, errors, loggeduser);
        }
        Book book = bookService.createNewBook(bookForm.getIsbn(), bookForm.getTitle(), bookForm.getAuthors(), bookForm.getEditorial(), bookForm.getDescription(), bookForm.getGenre(), bookForm.getEdition(), bookForm.getPublicationYear(), bookForm.isHardcover(), bookForm.isPocketEdition(), bookForm.getDimension(), bookForm.getLanguage(), bookForm.getPages(), bookForm.getWeight(), bookForm.getBookState(), bookForm.getRating(), bookForm.getImageFiles(), bookForm.getBookCover(), loggeduser);
        publicationService.createPublicationIfNeeded(bookForm.isPublish(), book.getBookId(), loggeduser.getUserId(), bookForm.getLocationId(), PublicationState.CURRENT);
        return new ModelAndView("redirect:/book");
    }


    // Screens
    /*
    // upload from preloaded book model
    @GetMapping("/book/new_book")
    public ModelAndView bookModelForm(@ModelAttribute("bookForm") BookForm bookForm, BindingResult errors, @ModelAttribute("loggedUser") User loggeduser) {

        ModelAndView mav = new ModelAndView("/book/new_book_form");

        mav.addObject("bookForm", bookForm);

        mav.addObject("genres", Genre.values());
        mav.addObject("bookStates", BookState.values());
        mav.addObject("languages", Language.values());
        mav.addObject("dimensions", BookDimension.values());
        mav.addObject("currentYear", Year.now().getValue());
        mav.addObject("step", 1);

        return mav;
    }*/

    /*
    @GetMapping("/book/new_book_model")
    public ModelAndView bookDetailsFormNewBook(@ModelAttribute(name = "bookDetailsForm") BookDetailsForm bookDetailsForm, @RequestParam("book_model_id") Long bookModelId, BindingResult errors, @ModelAttribute("loggedUser") User loggeduser) {

        ModelAndView mav = new ModelAndView("/book/book_form");

        BookModel bm = bookModelService.getBookModelByBookModelId(bookModelId);
        mav.addObject("bookDetailsForm", bookDetailsForm);
        mav.addObject("step", 2);
        mav.addObject("book_model", bm);
        mav.addObject("book_model_id", bookModelId);
        mav.addObject("bookStates", BookState.values());


        return mav;
    }*/

}
