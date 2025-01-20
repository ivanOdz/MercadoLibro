package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.webapp.auth.JwtTokenUtil;
import ar.edu.itba.paw.webapp.dto.User.*;
import ar.edu.itba.paw.webapp.dto.input.ReviewInputDTO;
import ar.edu.itba.paw.webapp.dto.output.ReviewDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("users")
@Component
public class UserController {

    @Autowired
    private UserService us;

    @Autowired
    private UserReviewService userReviewService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Context
    private UriInfo uriInfo;

    //Revisar VndType
    @POST
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response createUser(@Valid @NotNull final RegisterForm registerForm) {
        User user = us.createUser(registerForm.getUsername(), registerForm.getMail(), registerForm.getPassword(), LocaleContextHolder.getLocale().toLanguageTag());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getUserId().toString()).build()).build();
    }

    //Revisar UserDTO
    @GET
    @Path("/{id}")
    @Produces(value = {VndType.APPLICATION_USER})
    public Response getUser(@PathParam("id") final long userId) {

    	User user =  us.findById(userId);

    	UserDTO dto = UserDTO.fromUser(uriInfo, user);
    	GenericEntity<UserDTO> genericEntity = new GenericEntity<UserDTO>(dto) {};

    	return Response.ok(genericEntity).build();
    }

    //Revisar VndType
    @PATCH
    @Path("/{id}")
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response updateUser(@PathParam("id") final long userId, @Valid final UserUpdateDTO request) {
        us.updateUser(userId, request.getLanguage(), request.getNewUsername());

        return Response.noContent().build();
    }

    //Revisar VndType
    @POST
    @Path("/password-code")
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response createPasswordCode(@Valid EmailDTO emailDTO) {
        Integer passwordCode = us.changePasswordSolicited(emailDTO.getEmail());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(passwordCode.toString()).build()).build();
    }

    @PATCH
    @Path("/password-code/{code}")
    @Consumes(value = {VndType.USER_PASSWORD})
    public Response updatePassword(@PathParam("code") final int code, @Valid final PasswordChangeRequest request) {
        us.changePassword(code, request.getNewPassword());
        return Response.noContent().build();
    }

    // Podria no estar, pero por las dudas preguntar.
    /*@POST
    @Path("/verification-code")
    public Response createVerificationCode(){
        // TODO: Modularizar el service para agregar el funcionamiento aca.

        return Response.ok().build();
    }*/

    // TODO: Agregar refresh token via HttpOnly cookie.
    @POST
    @Path("/verification-code/{verification_code}")
    public Response verifyUser(@PathParam("verification_code") int verificationCode) {
        User user = us.verifyUser(verificationCode);

       return Response.noContent()
               .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.createToken(user))
               .header(HttpHeaders.SET_COOKIE, ).build();
    }

    // @GET /users/{id}/locations -> lista de ubicaciones del usuario

    // @GET /users/{id}/locations @QueryParam publicationId -> me devuelve lista de locations de una publicacion

    // @POST /users/{id}/locations -> Agrego una ubicacion al usuario. TODO: Setear maximo 5 Locations por usuario.

    @GET
    @Path("/{id}/locations")
    public Response getLocations(@PathParam("id") final long userId, @QueryParam("publication_id") final Integer publicationId) {

        final List<LocationDTO> locations = us.getLocations(userId, publicationId).stream()
                .map(location -> LocationDTO.fromLocation(uriInfo, location)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<LocationDTO>>(locations) {}).build();
    }

    @POST
    @Path("/{id}/locations")
    @Consumes(value = {VndType.APPLICATION_LOCATION})
    public Response createLocation(@PathParam("id") final long userId, String locationString) {
    	Location location = us.addLocation(userId, locationString);
		return Response.created(uriInfo.getAbsolutePathBuilder().path(location.getLocationId().toString()).build()).build();
    }
    
    @DELETE
    @Path("/{id}/locations/{location_id}")
    public Response removeLocation(@PathParam("id") final long userId, @PathParam("location_id") final long locationId) {
    	us.removeLocation(userId, locationId);
    	return Response.noContent().build();
    }

    @POST
    @Path("/reviews")
    @Consumes(value = {VndType.APPLICATION_USER_REVIEW})
    public Response createReview(@QueryParam("target_id") final Long targetId,
                                 @QueryParam("exchange_id") final Integer exchangeId,
                                 ReviewInputDTO reviewInputDTO) {
        UserReview ur = userReviewService.createUserReview(exchangeId, targetId, reviewInputDTO.getDescription(), reviewInputDTO.getRating());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(ur.getUserReviewId().toString()).build()).build();
    }

    @GET
    @Path("/reviews")
    @Produces(value = {VndType.APPLICATION_USER_REVIEW})
    public Response getReviews(@QueryParam("target_id") final Long targetId, @QueryParam("page") int page){
        PaginatedResponse<UserReview, BasicMetadata> reviews = userReviewService.getReviewsEarnedByUserId(targetId, page);
        List<ReviewDTO> reviewDTOS = reviews.getData().stream().map(review -> ReviewDTO.fromUserReview(uriInfo, review)).toList();
        return Response.ok(new GenericEntity<List<ReviewDTO>>(reviewDTOS) {}).build();
    }

    // /users/reviews/{id}
    @GET
    @Path("/reviews/{ur_id}")
    @Produces(value = {VndType.APPLICATION_USER_REVIEW})
    public Response getReview(@PathParam("ur_id") final Long reviewId) {
        final ReviewDTO userReviewDTO = ReviewDTO.fromUserReview(uriInfo, userReviewService.findUserReviewById(reviewId));

        return Response.ok(new GenericEntity<ReviewDTO>(userReviewDTO) {}).build();
    }

    @GET
    @Path("/{id}/rating")
    @Produces(value = {})
    public Response getUserAverageRating(@PathParam("id") final long userId) {
        final RatingDTO rating = RatingDTO.fromRating(uriInfo, userId, userReviewService.getUserRatingEarned(userId));

        return Response.ok(new GenericEntity<RatingDTO>(rating) {}).build();
    }
}




