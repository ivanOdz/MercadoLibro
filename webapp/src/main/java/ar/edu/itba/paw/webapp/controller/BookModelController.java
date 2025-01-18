package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.webapp.dto.Book.AuthorDTO;
import ar.edu.itba.paw.webapp.dto.input.BookModelDTO;
import ar.edu.itba.paw.webapp.dto.output.BookConditionDTO;
import ar.edu.itba.paw.webapp.dto.output.GenreDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GET
    @Path("/genres-summary")
    public Response getGenresSummary(@QueryParam("search") String search){
        List<GenreWrapper> genresSummary = bookModelService.getGenreWrapperList(search);
        List<GenreDTO> genres = genresSummary.stream().map(g -> GenreDTO.fromGenreWrapper(uriInfo, g)).toList();
        return Response.ok(new GenericEntity<List<GenreDTO>>(genres) {}).build();
    }
}
