package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.ExchangeService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.interfaces.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import ar.edu.itba.paw.webapp.auth.JwtTokenUtil;
import ar.edu.itba.paw.webapp.dto.User.*;
import ar.edu.itba.paw.webapp.dto.input.BookDTO;
import ar.edu.itba.paw.webapp.dto.input.ReviewInputDTO;
import ar.edu.itba.paw.webapp.dto.output.BookConditionDTO;
import ar.edu.itba.paw.webapp.dto.output.ExchangeDTO;
import ar.edu.itba.paw.webapp.dto.output.GenreDTO;
import ar.edu.itba.paw.webapp.dto.output.ReviewDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/users")
@Component
public class UserController {

    @Autowired
    private UserService us;

    @Autowired
    private ExchangeService es;

    @Autowired
    private UserReviewService userReviewService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @Produces(value = {VndType.APPLICATION_USER})
    public Response getUser(@PathParam("id") final long userId) {
    	
    	User user = null;
    	
    	try {
    		user = us.findById(userId);
    	} catch (UserNotFoundException e) {
    		return Response.status(Response.Status.NOT_FOUND).build();
		}
    	
    	UserDTO dto = UserDTO.fromUser(uriInfo, user);
    	GenericEntity<UserDTO> genericEntity = new GenericEntity<UserDTO>(dto) {};
    	
    	return Response.ok(genericEntity).build();
    }
    
    @POST
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response createUser(@Valid @NotNull final RegisterForm registerForm) {
        User user = us.createUser(registerForm.getUsername(), registerForm.getMail(), registerForm.getPassword(), LocaleContextHolder.getLocale().toLanguageTag());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getUserId().toString()).build()).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response updateUser(@PathParam("id") final long userId, @Valid final UserUpdateDTO request) {
        if (request.getLanguage() != null) {
            us.setUserLanguage(userId, request.getLanguage());
        }

        if (request.getNewUsername() != null) {
            us.changeUsername(userId, request.getNewUsername());
        }

        return Response.noContent().build();
    }

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

    @POST
    @Path("/verification-code")
    public Response createVerificationCode(){
        // Is it necessary?
        return Response.ok().build();
    }

    @POST
    @Path("/verification-code/{verification_code}")
    public Response verifyUser(@PathParam("verification_code") int verificationCode) {
        User user = us.verifyUser(verificationCode);

        return Response.noContent().header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.createToken(user)).build();
    }

    // 1) Podemos devolver arreglo de Strings en el mismo GET de users. 
    // 2) Endpoint separado de Location por userID | Hipervinculo en el GET de USERS (lista de URN's).
    @POST
    @Path("/{id}/locations")
    @Consumes(value = {VndType.APPLICATION_USER})
    public Response createLocation(@RequestParam Long userId, @RequestParam String locationString) {
    	Location location = us.addLocation(userId, locationString);
    	
		return Response.created(uriInfo.getAbsolutePathBuilder().path(location.toString()).build()).build();
    }
    
    @DELETE
    @Path("/{user_id}/locations/{location_id}")
    @Produces(value = {VndType.APPLICATION_USER})
    public Response removeLocation(@RequestParam Long userId, @RequestParam Long locationId) {
        
    	us.removeLocation(userId, locationId); // Podriamos ver de meterle que tire excepcion
        
    	return Response.noContent().build();
    }
    
    @GET
    @Path("/{id}/average_rating")
    @Produces(value = {VndType.APPLICATION_USER})
    public Response getUserAverageRating(@PathParam("id") final long userId) {
    	
    	User user = null;
    	
    	try {
    		user = us.findById(userId);
    	} catch (UserNotFoundException e) {
    		return Response.status(Response.Status.NOT_FOUND).build();
		}
    	
    	Rating rating = userReviewService.getUserRatingEarned(userId);
    	int count = rating.getRatingCount();
    	double average = rating.getRating();
    	
    	Map<String, Object> response = new HashMap<>();
    	
    	response.put("username", user.getUsername());
    	response.put("countRating", count);
    	response.put("averageRating", average);
    	
    	return Response.ok(response).build();
    }
    
    @RequestMapping("/profile")
    public ModelAndView profileHome(@RequestParam(name = "page", defaultValue = "0") int currentPage, @ModelAttribute("loggedUser") User loggeduser) {
        ModelAndView mav = new ModelAndView("profile/profile_home");

        mav.addObject("locationsUser", loggeduser.getUserLocations());
        mav.addObject("reviews", userReviewService.getReviewsEarnedByUserId(loggeduser.getUserId(), currentPage));
        //mav.addObject("userRating", userReviewService.getUserRatingEarned(loggeduser.getUserId()));

        return mav;
    }


    // Reviews

    @POST
    @Path("/{id}/reviews")
    public Response createReview(@PathParam("id") final long userId,
                                 @QueryParam("exchange_id") final Integer exchangeId,
                                 ReviewInputDTO reviewInputDTO) {
        userReviewService.createUserReview(exchangeId, userId, reviewInputDTO.getDescription(), reviewInputDTO.getRating());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(userId)).path("reviews").build()).build();
    }

    @GET
    @Path("/{id}/reviews")
    public Response getReviews(@PathParam("id") final long userId){
        PaginatedResponse<UserReview, BasicMetadata> reviews = userReviewService.getReviewsEarnedByUserId(userId, 0);
        List<ReviewDTO> reviewDTOS = reviews.getData().stream().map(review -> ReviewDTO.fromUserReview(uriInfo, review)).toList();
        return Response.ok(new GenericEntity<List<ReviewDTO>>(reviewDTOS) {}).build();
    }

    /*@RequestMapping("/check_verify")
    public ModelAndView checkVerify(@ModelAttribute("loggedUser") User loggeduser) {
        if (loggeduser.isVerified()) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/logout");
    }*/


    /*@RequestMapping(path = "/mail_input", method = RequestMethod.POST)
    public ModelAndView mailInputCheck(@Valid @ModelAttribute("mailForm") MailForm mailForm, BindingResult errors) {
        if(errors.hasErrors()) {
            return new ModelAndView("user/mail_input"); // Devuelve la vista explícitamente para mantener BindingResult
        }
        us.changePasswordSolicited(mailForm.getEmail());

        return new ModelAndView("redirect:/mail_input_message");
    }*/

        /*@RequestMapping(path = "/mail_input", method = RequestMethod.GET)
    public ModelAndView mailInput(@ModelAttribute("mailForm") MailForm mailForm) {
        ModelAndView modelAndView = new ModelAndView("user/mail_input");
        modelAndView.addObject("mailForm", new MailForm());
        return modelAndView;
    }*/


        /*@RequestMapping(path = "/change_password", method = RequestMethod.GET)
    public ModelAndView createPasswordForm(@ModelAttribute("passwordForm") PasswordForm passwordForm, @RequestParam(name = "verification_code") int verificationCode) {
        ModelAndView mav = new ModelAndView("user/new_password");
        mav.addObject("verification_code", verificationCode);
        return mav;
    }*/

    /*@RequestMapping(value = "/change_password", method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid @ModelAttribute("passwordForm") PasswordForm passwordForm, BindingResult errors, @RequestParam(name = "verification_code") int verificationCode) {
        if (errors.hasErrors()) {
            return createPasswordForm(passwordForm, verificationCode);
        }
        User user = us.changePassword(verificationCode, passwordForm.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return new ModelAndView("redirect:/success_password");
    }*/


    /*@PostMapping(value = "/changeUsername")
    public String changeUsername(@RequestParam("loggedUserId") long userId, @RequestParam("newUsername") String newUsername, RedirectAttributes redirectAttributes) {
        boolean updated = us.changeUsername(userId, newUsername);
        if (updated) {
            redirectAttributes.addFlashAttribute("message", "done");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "failed");
        }

        return "redirect:/profile";
    }*/

    /*@RequestMapping("/language")
    public ModelAndView changeLanguage(@RequestParam(name = "lang") String lang,  @ModelAttribute("loggedUser") User loggeduser) {
        Locale locale = Locale.forLanguageTag(lang);
        LocaleContextHolder.setLocale(locale);
        us.setUserLanguage(loggeduser, lang);

        return new ModelAndView("redirect:/profile");
    }*/
}