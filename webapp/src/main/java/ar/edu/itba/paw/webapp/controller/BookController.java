package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.webapp.dto.input.BookDTO;
import ar.edu.itba.paw.webapp.dto.input.BookStateDTO;
import ar.edu.itba.paw.webapp.dto.output.BookConditionDTO;
import ar.edu.itba.paw.webapp.dto.output.GenreDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;


@Path("books")
@Component
public class BookController {

    @Autowired
    private BookService bs;

    @Context
    private UriInfo uriInfo;

    // @GET /users/{id} -> UserDTO + Links(headers)

    // @GET /users/{id}/books -> Obtiene lista de libros de un usuario books

    /// next: users/{id}/books?page=2

    // @GET /books/{id} -> Obtiene 1 libro books

    //@GET /users/{id}/books/genres-summary -> Obtiene los resultados que hay agrupado por genero
    //@GET /users/{id}/books/condition-summary -> Obtiene los resultados que hay agrupado por condicion de libro

    //@GET /publications/condition-summary
    //@GET /publications/genres-summary

    //@GET /book-model/genres-summary

    // @GET /users/{id}/exchanges -> Lista de exchanges para el usuario x

    // @GET /exchanges/{id}



    @GET
    @Produces(value = {VndType.APPLICATION_BOOK})
    public Response getBooks(@QueryParam("owner") final long userId,
                             @QueryParam("search") @DefaultValue("")final String search,
                             @QueryParam("sort-type") @DefaultValue("BOOK_NAME_ASCENDING") final String sortType,
                             @QueryParam("is-book-state-filter-active") @DefaultValue("false") final String isBookStateFilterActive,
                             @QueryParam("book-state-filter") String bookStateFilter,
                             @QueryParam("genre-filter") final String genreFilter,
                             @QueryParam("page") @DefaultValue("0")final int currentPage,
                             @QueryParam("is-genre-filter-active") @DefaultValue("false") final String isGenreFilterActive) {
        PaginatedResponse<Book, ItemFilterMetadata> paginated = bs.getPaginatedBooks(search, isBookStateFilterActive,
                bookStateFilter, isGenreFilterActive, genreFilter, currentPage, userId, sortType);
        final List<BookDTO> books = paginated.getData().stream()
                .map(book -> BookDTO.fromBook(uriInfo, book)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<BookDTO>>(books) {}).build();
    }

    @POST
    @Consumes(value = {VndType.APPLICATION_BOOK})
    public Response postBook(final BookDTO bookDTO, @QueryParam("bookModel") final long bookModelId, @QueryParam("rating") final Integer rating) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book book = bs.createBook(bookModelId, loggedUser, BookState.valueOf(bookDTO.getState()), rating);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(book.getBookId())).build()).build();
    }

    @PATCH
    @Path("/{id}/state")
    @Consumes(value = {VndType.APPLICATION_BOOK_STATE})
    public Response updateBookState(@PathParam("id") final long bookId, @Valid final BookStateDTO bookState) {
        bs.updateBookState(bookId, bookState.getBookState());
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/images")
    public Response setImages(@PathParam("id") final long bookId, @QueryParam("image-id") final long imageId) {
        bs.setImage(bookId, imageId);
        return Response.noContent().build();
    }


    @GET
    @Path("/genres-summary")
    @Produces(value = {VndType.APPLICATION_GENRE_SUMMARY})
    public Response getGenresSummary(@QueryParam("owner") final long userId,
                                     @QueryParam("search") String search,
                                     @QueryParam("is-book-state-filter-active") String isBookStateFilterActive,
                                     @QueryParam("book-state-filter") String bookStateFilter) {
        List<GenreWrapper> genresSummary = bs.getGenreWrapperList(search, isBookStateFilterActive, bookStateFilter, userId);
        List<GenreDTO> genres = genresSummary.stream().map(g -> GenreDTO.fromGenreWrapper(uriInfo, g)).toList();
        return Response.ok(new GenericEntity<List<GenreDTO>>(genres) {}).build();
    }

    @GET
    @Path("/condition-summary")
    @Produces(value = {VndType.APPLICATION_CONDITION_SUMMARY})
    public Response getConditionSummary(@QueryParam("owner") final long userId,
                                        @QueryParam("search") String search,
                                        @QueryParam("is-genre-filter-active") String isGenreFilterActive,
                                        @QueryParam("genre-filter") String genreFilter) {
        List<BookStateWrapper> conditionSummary = bs.getBookStateWrapperList(search, isGenreFilterActive, genreFilter, userId);
        List<BookConditionDTO> conditions = conditionSummary.stream().map(g -> BookConditionDTO.fromBookState(uriInfo, g)).toList();
        return Response.ok(new GenericEntity<List<BookConditionDTO>>(conditions) {}).build();
    }


    // Screens
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


        List<GenreWrapper> genreWrapperList = bs.getGenreWrapperList(search, isBookStateFilterActive, bookStateFilter, loggeduser.getUserId());
        List<BookStateWrapper> bookStateWrapperList = bs.getBookStateWrapperList(search, isGenreFilterActive, genreFilter, loggeduser.getUserId());

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


}
