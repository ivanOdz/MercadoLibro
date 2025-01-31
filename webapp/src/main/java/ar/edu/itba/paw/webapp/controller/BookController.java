package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.webapp.dto.input.BookImageDTO;
import ar.edu.itba.paw.webapp.dto.input.BookInputDTO;
import ar.edu.itba.paw.webapp.dto.output.BookDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import ar.edu.itba.paw.webapp.utils.PageResponseUtil;
import ar.edu.itba.paw.webapp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Path("books")
@Component
public class BookController {

    @Autowired
    private BookService bs;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {VndType.APPLICATION_BOOK})
    public Response getBooks(@QueryParam("owner") final long userId,
                             @QueryParam("search") @DefaultValue("")final String search,
                             @QueryParam("sort") @DefaultValue("BOOK_NAME_ASCENDING") final String sortType,
                             @QueryParam("state") String state,
                             @QueryParam("genre") final String genre,
                             @QueryParam("page") @DefaultValue("0")final int currentPage) {
        PaginatedResponse<Book, ItemFilterMetadata> paginated = bs.getPaginatedBooks(search, state, genre, currentPage, userId, sortType);
        final List<BookDTO> books = paginated.getData().stream().map(book -> BookDTO.fromBook(uriInfo, book)).collect(Collectors.toList());
        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<BookDTO>>(books) {});

        List<GenreWrapper> genresSummary = bs.getGenreWrapperList(search, state, userId);
        List<BookStateWrapper> conditionSummary = bs.getBookStateWrapperList(search, genre, userId);

        Map<String, String> genreHeaders = SerializationUtils.serializeGenreWrapper(genresSummary);
        genreHeaders.forEach(response::header);

        Map<String, String> conditionHeaders = SerializationUtils.serializeConditionWrapper(conditionSummary);
        conditionHeaders.forEach(response::header);

        return PageResponseUtil.getResponse(currentPage, paginated.getMetadata().getMaxPage(), uriInfo, response);
    }

    // CHECK: bad request is returned if the userId does not match logged user
    @POST
    @Consumes(value = {VndType.APPLICATION_BOOK_INPUT})
    public Response postBook(final BookInputDTO bookDTO) {
        Book book = bs.createBook(bookDTO.getBookModelUrn(), bookDTO.getUserUrn(), BookState.valueOf(bookDTO.getCondition()), bookDTO.getRating(), bookDTO.getImagesUrns());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(book.getBookId())).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(value = {VndType.APPLICATION_BOOK})
    public Response updateBook(@PathParam("id") final long bookId, @Valid final BookDTO book) {
        bs.updateBook(bookId, book.getState());
        return Response.noContent().build();
    }

}
