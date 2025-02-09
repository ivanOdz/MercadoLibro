package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.webapp.dto.input.BookModelDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import ar.edu.itba.paw.webapp.utils.CacheResponseUtil;
import ar.edu.itba.paw.webapp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;

@Path("book_models")
@Component
public class BookModelController {

    @Autowired
    private BookModelService bookModelService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {VndType.APPLICATION_BOOK_MODEL})
    public Response getBookModels(@QueryParam("search") @DefaultValue("")final String search,
                                  @QueryParam("genre")final String genre,
                                  @QueryParam("page") @DefaultValue("0") final int currentPage,
                                  @QueryParam("sort") @DefaultValue("BOOK_NAME_ASCENDING") String sortType,
                                  @Context Request request) {

        PaginatedResponse<BookModel, BookModelMetadata> modelBooks = bookModelService.getPaginatedBookModels(search, genre, currentPage, sortType);
        List<BookModelDTO> bookModels = modelBooks.getData().stream().map(bm -> BookModelDTO.fromBookModel(uriInfo, bm)).toList();

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<BookModelDTO>>(bookModels) {});

        List<GenreWrapper> genresSummary = bookModelService.getGenreWrapperList(search);

        Map<String, String> genreHeaders = SerializationUtils.serializeGenreWrapper(genresSummary);
        genreHeaders.forEach(response::header);

        return CacheResponseUtil.conditionalCacheResponse(request, new EntityTag(Integer.toString(modelBooks.getData().hashCode())));
    }


    // IMPLEMENT
    @GET
    @Path("/{id}")
    @Produces(value = {VndType.APPLICATION_BOOK_MODEL})
    public Response getBookModel(@PathParam("id") Long bookModelId) {
        BookModel bookModel = bookModelService.getBookModelByBookModelId(bookModelId);
        return Response.ok(BookModelDTO.fromBookModel(uriInfo, bookModel)).build();
    }

    @POST
    @Consumes(value = {VndType.APPLICATION_BOOK_MODEL})
    public Response postBookModel(final BookModelDTO bookModelDTO, @QueryParam("rating") final Integer rating) {
        BookModel bookModel = bookModelService
                .createBookModel(bookModelDTO.getIsbn(), bookModelDTO.getTitle(), bookModelDTO.getEditorial(),
                        bookModelDTO.getDescription(), Genre.valueOf(bookModelDTO.getGenre()), bookModelDTO.getEdition(),
                        bookModelDTO.getPublicationYear(), bookModelDTO.getHardcover(), bookModelDTO.getPocketEdition(),
                        BookDimension.valueOf(bookModelDTO.getDimension()), Language.valueOf(bookModelDTO.getBookLanguage()),
                        bookModelDTO.getPages(),bookModelDTO.getWeight(), bookModelDTO.getAuthors());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(bookModel.getBookModelId())).build()).build();
    }

    @PATCH
    @Path("{id}")
    @Consumes(value = {VndType.APPLICATION_BOOK_MODEL})
    public Response setBookCover(@PathParam("id") Long bookModelId, final BookModelDTO bookModelDTO) {
        bookModelService.setCover(bookModelId, bookModelDTO.getCoverId());
        return Response.noContent().build();
    }


}
