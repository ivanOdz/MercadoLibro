package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import ar.edu.itba.paw.webapp.dto.BookDTO;
import ar.edu.itba.paw.webapp.dto.BookModelDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("/book_models")
@Component
public class BookModelController {

    @Autowired
    private BookModelService bookModelService;
    @Context
    private UriInfo uriInfo;
    /*
    // ASK because it should be a /books/book_model but that is not RESTful
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

}
