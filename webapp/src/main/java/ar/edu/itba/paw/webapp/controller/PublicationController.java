package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;

import ar.edu.itba.paw.webapp.dto.Publication.FavoriteDTO;
import ar.edu.itba.paw.webapp.dto.Publication.PublicationCreationDTO;
import ar.edu.itba.paw.webapp.dto.Publication.PublicationDTO;
import ar.edu.itba.paw.webapp.dto.Publication.PublicationUpdateDTO;
import ar.edu.itba.paw.webapp.dto.User.UserDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import ar.edu.itba.paw.webapp.utils.PageResponseUtil;
import ar.edu.itba.paw.webapp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    // Authorization required
    // If favorites=true & userId == null -> 403 Forbidden ????
    // TODO: Hacer los filtros para las publicaciones favoritas.
    @GET
    @Produces(value = {VndType.APPLICATION_PUBLICATION})
    public Response getPublications(@QueryParam("search") @DefaultValue("")final String search,
                                       @QueryParam("sort") @DefaultValue("DEFAULT_PUBLICATION_SORT_TYPE") final String sortType,
                                       @QueryParam("state") String state,
                                       @QueryParam("genre") final String genre,
                                       @QueryParam("page") @DefaultValue("0")final int currentPage,
                                       @QueryParam("size") @DefaultValue("0") Integer size,
                                       @QueryParam("user-id") long userId,
                                       @QueryParam("favorites") @DefaultValue("false") boolean favorites) {

        PaginatedResponse<Publication, ItemFilterMetadata> publications = ps.getPaginatedPublications(search,
                state, genre, sortType, currentPage, userId, favorites);

        List<PublicationDTO> publicationDTOList = publications.getData().stream()
                .map(publication -> PublicationDTO.fromPublication(uriInfo, publication)).collect(Collectors.toList());;

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<PublicationDTO>>(publicationDTOList) {});


        List<GenreWrapper> genresSummary = ps.getMyGenreWrapperList(userId, search, state);
        List<BookStateWrapper> conditionSummary = ps.getBookStateWrapperList(search, genre);

        Map<String, String> genreHeaders = SerializationUtils.serializeGenreWrapper(genresSummary);
        genreHeaders.forEach(response::header);

        Map<String, String> conditionHeaders = SerializationUtils.serializeConditionWrapper(conditionSummary);
        conditionHeaders.forEach(response::header);

        return PageResponseUtil.getResponse(currentPage, publications.getMetadata().getMaxPage(), uriInfo, response);
    }

    // Authorization required
    // Usuario debe estar logueado y debe ser dueño del libro y de la location
    @POST
    @Consumes(value = {VndType.APPLICATION_PUBLICATION})
    public Response postPublication(final PublicationCreationDTO publicationDTO) {
        Publication publication = ps.createPublication(publicationDTO.getBookURN(), publicationDTO.getUserURN(), publicationDTO.getLocationURN());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(publication.getPublicationId().toString()).build()).build();
    }

    // Authorization required
    // Usuario debe estar logueado y debe ser dueño de la publicacion
    @DELETE
    @Path("/{publication_id}")
    public Response deletePublication(@PathParam("publication_id") Long publicationId) {
        ps.deletePublication(publicationId);
        return Response.noContent().build();
    }

    // Authorization Required
    // Si la publicacion esta CURRENT, todos la pueden acceder
    // Si la publicacion esta OFFERED, solo el dueño la puede ver (chequear usuario logueado es dueño de la pub, sino 403)
    @GET
    @Path("/{publication_id}")
    @Produces(value = {VndType.APPLICATION_PUBLICATION})
    public Response getPublication(@PathParam("publication_id") Long publicationId) {
        Publication publication = ps.getActivePublication(publicationId);
        PublicationDTO dto = PublicationDTO.fromPublication(uriInfo, publication);
        GenericEntity<PublicationDTO> genericEntity = new GenericEntity<PublicationDTO>(dto) {};

        return Response.ok(genericEntity).build();
    }

    // Authorization Required
    // El usuario debe estar logueado y el id del usuario logueado debe matchear con el del UserDTO (sacado del self)
    @POST
    @Path("/{publication_id}/favorite")
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response createFavoritePublication(@PathParam("publication_id") Long publicationId, final UserDTO userDTO){
        FavoritePublication fp = ps.likePublication(publicationId, userDTO.getSelf());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(fp.getFavoritepublicationId().toString()).build()).build();
    }

    // Authorization Required
    // El usuario debe estar logueado y debe ser dueño de la publicacion favorita a eliminar
    /* Seria un problema que el publicationId no lo estamos usando?? */
    @DELETE
    @Path("/{publication_id}/favorite/{id}")
    public Response deleteFavoritePublication(@PathParam("publication_id") Long publicationId, @PathParam("id") Long fpId){
        ps.deleteFavoritePublication(fpId);

        return Response.noContent().build();
    }

    // Authorization Required
    // El usuario debe estar logueado y debe ser dueño de la publicacion favorita
    @GET
    @Path("/{publication_id}/favorite/{id}")
    @Produces(value = {VndType.APPLICATION_FAVORITE_PUBLICATION})
    public Response getFavoritePublicationById(@PathParam("publication_id") Long publicationId, @PathParam("id") Long fpId){
        FavoritePublication fp = ps.getFavoritePublicationById(fpId);

        FavoriteDTO dto = FavoriteDTO.fromFavoritePublication(uriInfo, fp);
        GenericEntity<FavoriteDTO> genericEntity = new GenericEntity<FavoriteDTO>(dto) {};

        return Response.ok(genericEntity).build();
    }

    // Authorization Required
    // El usuario debe estar logueado y debe coincidir su id con el que se manda via query param

    /* ¿Deberiamos contemplar el caso en donde no se pasa ningun query param a este endpoint?*/
    /* No tiene sentido pedir los favoritos sin especificar de quien estamos pidiendo */
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
