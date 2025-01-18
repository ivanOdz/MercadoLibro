package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.webapp.dto.input.BookDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    private BookService bookService;

    @Context
    private UriInfo uriInfo;

    @PATCH
    @Path("/{id}/state")
    @Consumes(value = {VndType.APPLICATION_BOOK_STATE})
    public Response updateBookState(@PathParam("id") final long bookId, final String bookState) {
        bookService.updateBookState(bookId, bookState);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/images")
    public Response setImages(@PathParam("id") final long bookId, @QueryParam("image-id") final long imageId) {
        bookService.setImage(bookId, imageId);
        return Response.noContent().build();
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
}
