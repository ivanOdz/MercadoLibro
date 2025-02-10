package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.webapp.auth.JwtTokenUtil;
import ar.edu.itba.paw.webapp.dto.input.*;
import ar.edu.itba.paw.webapp.dto.output.*;
import ar.edu.itba.paw.webapp.dto.input.UserUpdateDTO;
import ar.edu.itba.paw.webapp.dto.output.ReviewDTO;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.dto.output.UserDTO;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;

import ar.edu.itba.paw.webapp.utils.CacheResponseUtil;
import ar.edu.itba.paw.webapp.utils.PageResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


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

    @POST
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response createUser(@Valid @NotNull final RegisterDTO registerDTO) {
        User user = us.createUser(registerDTO.getUsername(), registerDTO.getMail(), registerDTO.getPassword(), LocaleContextHolder.getLocale().toLanguageTag());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getUserId().toString()).build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {VndType.APPLICATION_USER})
    public Response getUser(@PathParam("id") final Long id) {
        User user = us.findById(id);
        Rating userRating = userReviewService.getUserRatingEarned(user.getUserId());

        UserDTO dto = UserDTO.fromUser(uriInfo, user, userRating);
        GenericEntity<UserDTO> genericEntity = new GenericEntity<UserDTO>(dto) {};
        return Response.ok(genericEntity).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response updateUser(@PathParam("id") final Long id, @Valid final UserUpdateDTO request) {
        User user = us.updateUser(id, request.getLanguage(), request.getNewUsername());
        if(user == null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.noContent()
                .header("X-User-Uri", "/api/users/" + user.getUserId())
                .build();
    }

    @POST
    @Consumes(value = {VndType.APPLICATION_USER_EMAIL})
    public Response createPasswordCode(@Valid EmailDTO emailDTO) {
        us.changePasswordSolicited(emailDTO.getEmail());
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{password-token}")
    @Consumes(value = {VndType.APPLICATION_USER_PASSWORD})
    public Response updatePassword(@PathParam("password-token") final int code,
                                   @Valid final PasswordChangeDTO request) {
        us.changePassword(code, request.getNewPassword());
        return Response.noContent().build();
    }


    // TODO: Manejar en los filtros el caso que la cuenta no esta verificada.
    @POST
    @Consumes(value = {VndType.APPLICATION_VERIFICATION_CODE})
    public Response verifyUser(final VerificationDTO verificationDTO) {
        User user = us.verifyUser(verificationDTO.getVerificationCode());

        String accessToken = jwtTokenUtil.createAccessToken(user);
        String refreshToken = jwtTokenUtil.createRefreshToken(user);

        return Response.noContent()
                .header("X-User-Uri", "/api/users/" + user.getUserId())
                .header(JwtTokenUtil.ACCESS_TOKEN_HEADER, accessToken) // access token
                .header(JwtTokenUtil.REFRESH_TOKEN_HEADER, refreshToken)  // refresh token
                .build();
    }

    @POST
    @Path("/{id}/locations")
    @Consumes(value = {VndType.APPLICATION_LOCATION})
    public Response createLocation(@PathParam("id") final long userId, LocationCreation locationCreation) {
        Location location = us.addLocation(userId, locationCreation.getLocation());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(location.getLocationId().toString()).build()).build();
    }

    @GET
    @Path("/{id}/locations/{location_id}")
    @Produces(value = {VndType.APPLICATION_LOCATION})
    public Response getLocation(@PathParam("id") final long userId, @PathParam("location_id") final long locationId) {
        Location location = us.getLocation(locationId);
        LocationDTO locationDTO = LocationDTO.fromLocation(uriInfo, userId, location);

        return Response.ok(new GenericEntity<LocationDTO>(locationDTO) {}).build();
    }

    @GET
    @Path("/{id}/locations")
    @Produces(value = {VndType.APPLICATION_LOCATION})
    @PreAuthorize("@accessControl.getUserLocationsAccess(#publicationId)")
    public Response getLocations(@PathParam("id") final long userId, @QueryParam("publication_id") final Long publicationId) {
        final List<LocationDTO> locations = us.getLocations(userId, publicationId).stream()
                .map(location -> LocationDTO.fromLocation(uriInfo, userId, location)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<LocationDTO>>(locations) {}).build();
    }

    @DELETE
    @Path("/{id}/locations/{location_id}")
    public Response removeLocation(@PathParam("id") final long userId, @PathParam("location_id") final long locationId) {
    	us.removeLocation(userId, locationId);

    	return Response.noContent().build();
    }


    @POST
    @Path("{id}/reviews")
    @Consumes(value = {VndType.APPLICATION_USER_REVIEW})
    @PreAuthorize("@accessControl.createReviewAccess(#targetId, #reviewInputDTO)")
    public Response createReview(@PathParam("id") final Long targetId,
                                 ReviewDTO reviewInputDTO) {
        UserReview ur = userReviewService.createUserReview(reviewInputDTO.getExchangeId(), targetId, reviewInputDTO.getDescription(), reviewInputDTO.getRating());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(ur.getUserReviewId().toString()).build()).build();
    }

    @GET
    @Path("{id}/reviews")
    @Produces(value = {VndType.APPLICATION_USER_REVIEW})
    @PreAuthorize("@accessControl.reviewListAccess(#targetId)")
    public Response getReviews(@PathParam("id") final Long targetId, @QueryParam("page") int page){
        PaginatedResponse<UserReview, BasicMetadata> reviews = userReviewService.getReviewsEarnedByUserId(targetId, page);
        List<ReviewDTO> reviewDTOS = reviews.getData().stream().map(review -> ReviewDTO.fromUserReview(uriInfo, review)).toList();
        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<ReviewDTO>>(reviewDTOS) {});
        Response paginated_response = PageResponseUtil.getResponse(page, reviews.getMetadata().getMaxPage(), uriInfo, response);
        return CacheResponseUtil.unconditionalCacheResponse(Response.fromResponse(paginated_response));
    }

    /**
     * @GET /users/{id}/reviews/{ur_id} -> Si el usuario de id {id} es participe de la review {ur_id},
     * entonces se retorna la review. Caso contrario, 404 - Not Found (Service)
     **/
    @GET
    @Path("{id}/reviews/{ur_id}")
    @Produces(value = {VndType.APPLICATION_USER_REVIEW})
    public Response getReview(@PathParam("id") final Long targetId, @PathParam("ur_id") final Long reviewId) {
        final ReviewDTO userReviewDTO = ReviewDTO.fromUserReview(uriInfo, userReviewService.findUserReviewById(targetId, reviewId));
        Response.ResponseBuilder response = Response.ok(new GenericEntity<ReviewDTO>(userReviewDTO) {});
        return CacheResponseUtil.unconditionalCacheResponse(response);
    }
}




