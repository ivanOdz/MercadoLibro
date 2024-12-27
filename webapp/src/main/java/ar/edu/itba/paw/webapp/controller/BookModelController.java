package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.webapp.dto.Book.AuthorDTO;
import ar.edu.itba.paw.webapp.dto.input.BookModelDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("book_models")
@Component
public class BookModelController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookModelService bookModelService;

    @Context
    private UriInfo uriInfo;

    /*
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
    @Produces(value = {VndType.APPLICATION_BOOK_MODEL})
    public Response getBookModels(@QueryParam("search") @DefaultValue("")final String search,
                                  @QueryParam("is-genre-filter-active") @DefaultValue("false")final String isGenreFilterActive,
                                  @QueryParam("genre-filter")final String genreFilter,
                                  @QueryParam("page") @DefaultValue("0") final int currentPage,
                                  @QueryParam("sort-type") @DefaultValue("BOOK_NAME_ASCENDING") String sortType) {

        PaginatedResponse<BookModel, BookModelMetadata> modelBooks = bookModelService.getPaginatedBookModels(search, isGenreFilterActive, genreFilter, currentPage, sortType);
        List<BookModelDTO> bookModels = modelBooks.getData().stream().map(bm -> BookModelDTO.fromBookModel(uriInfo, bm)).toList();

        return Response.ok(new GenericEntity<List<BookModelDTO>>(bookModels) {}).build();
    }


    /*
    @PostMapping("/book/create_book")
    public ModelAndView createBook(@Valid @ModelAttribute(name = "bookDetailsForm") BookDetailsForm bookDetailsForm, BindingResult errors, @RequestParam("book_model_id") Long bookModelId, @ModelAttribute("loggedUser") User loggeduser) {
        if (errors.hasErrors()) {
            return bookDetailsFormNewBook(bookDetailsForm, bookModelId, errors, loggeduser);
        }
        Book book = bookService.createBook(bookModelId, bookDetailsForm.getBookState(), bookDetailsForm.getRating(), bookDetailsForm.getImageFiles(), bookDetailsForm.getBookCover(), null, loggeduser, false);

        publicationService.createPublicationIfNeeded(bookDetailsForm.isPublish(), book.getBookId(), loggeduser.getUserId(), bookDetailsForm.getLocationId(), PublicationState.CURRENT);
        return new ModelAndView("redirect:/book");
    }*/


    @POST
    @Consumes(value = {VndType.APPLICATION_BOOK_MODEL})  // /book_models
    public Response postBookModel(final BookModelDTO bookModelDTO, @QueryParam("rating") final Integer rating) {
        BookModel bookModel = bookModelService.createBookModel(bookModelDTO.getIsbn(), bookModelDTO.getTitle(), bookModelDTO.getEditorial(), bookModelDTO.getDescription(), Genre.valueOf(bookModelDTO.getGenre()), bookModelDTO.getEdition(),bookModelDTO.getPublicationYear(), bookModelDTO.getHardcover(), bookModelDTO.getPocketEdition(), BookDimension.valueOf(bookModelDTO.getDimension()), Language.valueOf(bookModelDTO.getBookLanguage()),bookModelDTO.getPages(),bookModelDTO.getWeight(), bookModelDTO.getAuthors());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(bookModel.getBookModelId())).build()).build();
    }

//    @PATCH
//    @Path("{id}/authors")  // /book_models/{id}/authors
//    @Consumes(value = {VndType.APPLICATION_AUTHOR})
//    public Response setAuthor(@PathParam("id") Long bookModelId, AuthorDTO authorDTO) {
//        BookModel bookModel = bookModelService.addAuthor(bookModelId, authorDTO.getName());
//        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(bookModel.getBookModelId())).build()).build();
//    }

    @PATCH
    @Path("{id}/cover") // /book_models/{id}/cover
    @Consumes(value = {VndType.APPLICATION_BOOK_COVER})
    public Response setBookCover(@PathParam("id") Long bookModelId, @QueryParam("image-id") Long imageId) {
        BookModel bookModel = bookModelService.setCover(bookModelId, imageId);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(bookModel.getBookModelId())).build()).build();
    }
}
