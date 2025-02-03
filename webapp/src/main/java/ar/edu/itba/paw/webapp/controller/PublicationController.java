package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import ar.edu.itba.paw.webapp.dto.input.PublicationInputDTO;
import ar.edu.itba.paw.webapp.dto.output.FavoriteDTO;
import ar.edu.itba.paw.webapp.dto.output.PublicationDTO;
import ar.edu.itba.paw.webapp.dto.output.UserDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import ar.edu.itba.paw.webapp.utils.PageResponseUtil;
import ar.edu.itba.paw.webapp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("publications")
@Component
public class PublicationController {

    @Autowired
    private PublicationService ps;

    @Context
    private UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @GET
    @Produces(value = {VndType.APPLICATION_PUBLICATION})
    public Response getPublications(@QueryParam("search") @DefaultValue("")final String search,
                                       @QueryParam("sort") @DefaultValue("DEFAULT_PUBLICATION_SORT_TYPE") final String sortType,
                                       @QueryParam("state") String state,
                                       @QueryParam("genre") final String genre,
                                       @QueryParam("page") @DefaultValue("0")final int currentPage,
                                       @QueryParam("size") @DefaultValue("0") Integer size,
                                       @QueryParam("user_id") Long userId,
                                       @QueryParam("location_id") Long locationId,
                                       @QueryParam("favorites") @DefaultValue("false") Boolean favorites) {

        PaginatedResponse<Publication, ItemFilterMetadata> publications = ps.getPaginatedPublications(search,
                state, genre, sortType, currentPage, userId, favorites, locationId);

        List<PublicationDTO> publicationDTOList = publications.getData().stream()
                .map(publication -> PublicationDTO.fromPublication(uriInfo, publication)).collect(Collectors.toList());

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<PublicationDTO>>(publicationDTOList) {});


        List<GenreWrapper> genresSummary = ps.getMyGenreWrapperList(userId, search, state);
        List<BookStateWrapper> conditionSummary = ps.getBookStateWrapperList(search, genre);

        Map<String, String> genreHeaders = SerializationUtils.serializeGenreWrapper(genresSummary);
        genreHeaders.forEach(response::header);

        Map<String, String> conditionHeaders = SerializationUtils.serializeConditionWrapper(conditionSummary);
        conditionHeaders.forEach(response::header);

        return PageResponseUtil.getResponse(currentPage, publications.getMetadata().getMaxPage(), uriInfo, response);
    }


    @POST
    @Consumes(value = {VndType.APPLICATION_PUBLICATION})
    @PreAuthorize("@accessControl.publicationsPostAccess(#publicationDTO)")
    public Response postPublication(final PublicationInputDTO publicationDTO) {
        Publication publication = ps.createPublication(publicationDTO.getBookId(), publicationDTO.getUserId(), publicationDTO.getLocationId());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(publication.getPublicationId().toString()).build()).build();
    }


    @DELETE
    @Path("/{publication_id}")
    public Response deletePublication(@PathParam("publication_id") Long publicationId) {
        ps.deletePublication(publicationId);
        return Response.noContent().build();
    }


    @GET
    @Path("/{publication_id}")
    @Produces(value = {VndType.APPLICATION_PUBLICATION})
    public Response getPublication(@PathParam("publication_id") Long publicationId) {
        Publication publication = ps.getActivePublication(publicationId);
        PublicationDTO dto = PublicationDTO.fromPublication(uriInfo, publication);
        GenericEntity<PublicationDTO> genericEntity = new GenericEntity<PublicationDTO>(dto) {};

        return Response.ok(genericEntity).build();
    }


    @POST
    @Path("/{publication_id}/favorite")
    @Consumes(value = {VndType.APPLICATION_USER})
    @PreAuthorize("@accessControl.publicationsFavoritePostAccess(#publicationId,#userDTO)")
    public Response createFavoritePublication(@PathParam("publication_id") Long publicationId, final UserDTO userDTO){
        FavoritePublication fp = ps.likePublication(publicationId, userDTO.getSelf());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(fp.getFavoritepublicationId().toString()).build()).build();
    }


    @DELETE
    @Path("/{publication_id}/favorite/{favorite_id}")
    @PreAuthorize("@accessControl.publicationsFavoriteListAccess(#publicationId, #fpId)")
    public Response deleteFavoritePublication(@PathParam("publication_id") Long publicationId, @PathParam("favorite_id") Long fpId){
        ps.deleteFavoritePublication(fpId);
        return Response.noContent().build();
    }


    @GET
    @Path("/{publication_id}/favorite/{favorite_id}")
    @Produces(value = {VndType.APPLICATION_FAVORITE_PUBLICATION})
    public Response getFavoritePublicationById(@PathParam("publication_id") Long publicationId, @PathParam("favorite_id") Long fpId){
        FavoritePublication fp = ps.getFavoritePublicationById(fpId);

        FavoriteDTO dto = FavoriteDTO.fromFavoritePublication(uriInfo, fp);
        GenericEntity<FavoriteDTO> genericEntity = new GenericEntity<FavoriteDTO>(dto) {};

        return Response.ok(genericEntity).build();
    }

    @GET
    @Path("/{publication_id}/favorite")
    @Produces(value = {VndType.APPLICATION_FAVORITE_PUBLICATION})
    public Response getFavoritePublication(@PathParam("publication_id") Long publicationId, @QueryParam("user_id") Long userId){
        FavoritePublication fp = ps.getFavoritePublicationFromUser(publicationId, userId);

        FavoriteDTO dto = FavoriteDTO.fromFavoritePublication(uriInfo, fp);
        GenericEntity<FavoriteDTO> genericEntity = new GenericEntity<FavoriteDTO>(dto) {};

        return Response.ok(genericEntity).build();
    }

    // TODO: Preguntar cual deberia de ser la forma asociar una location a una publication, si PATCH o POST.
    // Authorization Required
    // El usuario tiene que estar logueado y debe ser dueño de la publicacion
    // Creo que este metodo se reemplaza por el de la location
    /*@PATCH
    @Path("/{publication_id}")
    @Consumes(value = {VndType.APPLICATION_PUBLICATION})
    public Response updatePublication(@PathParam("publication_id") Long publicationId, PublicationUpdateDTO publicationUpdateDTO) {
        ps.updatePublication(publicationDTO.getLocations());

        return Response.noContent().build();
    }


    // Revisar porque creo que al hacer POST, tengo que devolver donde se crea. No es una location lo que se crea, sino
    // otra entidad llamada Publication/Location. Por lo tanto al crearla, tengo que poder accederla y eliminarla. Creo.
    @POST
    @Path("/{publication_id}/locations")
    @Consumes(value = {VndType.APPLICATION_LOCATION})
    public Response addLocationToPublication(@PathParam("publication_id") Long publicationId, @QueryParam("location_id") final long locationId)  {

        ps.addLocation(publicationId, locationId, loggeduser);

        return Response.noContent().build();
    }*/
}
