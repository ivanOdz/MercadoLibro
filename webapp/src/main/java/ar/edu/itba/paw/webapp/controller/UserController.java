package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.form.PasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Controller
public class UserController {

    @Autowired
    private UserService us;

    @Autowired
    private PawUserDetailsService userDetailsService;

    @Autowired
    private UserReviewService userReviewService;

    @Autowired
    private MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView modelAndView = new ModelAndView("user/login");

        if (error != null) {
            Locale locale = LocaleContextHolder.getLocale();

            String errorMessage = messageSource.getMessage("login.invalid", null, locale);
            modelAndView.addObject("error", errorMessage);
        }

        if (logout != null) {
            modelAndView.addObject("message", "Has cerrado sesión correctamente.");
        }

        return modelAndView;
    }

    @RequestMapping("/verification")
    public ModelAndView verificationController(@RequestParam(name = "verification_code") int verificationCode) {

        User user;
        try {
            user = us.getUserToVerify(verificationCode);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        try {
            us.verifyUser(verificationCode);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        LOGGER.info(messageSource.getMessage("info.user.verified", null, LocaleContextHolder.getLocale()), user.getUsername());

        return new ModelAndView("redirect:/success_verification");
    }

    @RequestMapping("/check_verify")
    public ModelAndView checkVerify() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            if (pud.getUser().isVerified()) {
                return new ModelAndView("redirect:/");
            }
        }
        return new ModelAndView("redirect:/logout");
    }

    @RequestMapping("/mail_input")
    public ModelAndView mailInput() {
        return new ModelAndView("user/mail_input");
    }

    @RequestMapping("/change_password_solicited")
    public ModelAndView changePasswordSolicited(@RequestParam(name = "email") String email) {

        try {
            us.changePasswordSolicited(email);
        } catch (BadRequestException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        } catch (NotFoundException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        return new ModelAndView("redirect:/mail_input_message");
    }

    @RequestMapping(path = "/change_password", method = RequestMethod.GET)
    public ModelAndView createPasswordForm(@ModelAttribute("passwordForm") PasswordForm passwordForm, @RequestParam(name = "verification_code") int verificationCode) {
        ModelAndView mav = new ModelAndView("user/new_password");
        mav.addObject("verification_code", verificationCode);
        return mav;
    }

    @RequestMapping(value = "/change_password", method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid @ModelAttribute("passwordForm") PasswordForm passwordForm, BindingResult errors, @RequestParam(name = "verification_code") int verificationCode) {
        if (errors.hasErrors()) {
            LOGGER.info("Password form has errors. Redirecting to password form");
            return createPasswordForm(passwordForm, verificationCode);
        }

        try {
            us.changePassword(verificationCode, passwordForm.getPassword());
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        }

        LOGGER.info(messageSource.getMessage("info.password.changed", null, LocaleContextHolder.getLocale()));

        return new ModelAndView("redirect:/success_password");
    }

    @PostMapping(value = "/changeUsername")
    public String changeUsername(@RequestParam("loggedUserId") long userId, @RequestParam("newUsername") String newUsername, RedirectAttributes redirectAttributes) {

        boolean updated;
        try{
            updated = us.changeUserName(userId, newUsername);
        }catch (ApplicationRuntimeException e){
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return "redirect:/400";
        }

        if (updated) {
            redirectAttributes.addFlashAttribute("message", "done");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "failed");
        }

        return "redirect:/profile";
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("userForm") UserForm userForm) {
        return new ModelAndView("user/create");
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request,
                               @Valid @ModelAttribute("userForm") UserForm userForm,
                               BindingResult errors) {

        if (errors.hasErrors()) {
            return createForm(userForm);
        }

        if (us.userExists(userForm.getMail())) {
            errors.rejectValue("mail", "error.user.exists");
            return createForm(userForm);
        }

        User user = us.createUser(userForm.getUsername(), userForm.getMail(), userForm.getPassword(), LocaleContextHolder.getLocale().toLanguageTag());
        return new ModelAndView("redirect:/success_registration");
    }

    // success screens

    @RequestMapping("/success_registration")
    public ModelAndView successRegistration() {
        return new ModelAndView("user/success_registration");
    }

    @RequestMapping("/success_verification")
    public ModelAndView successVerification() {
        return new ModelAndView("user/success_verification");
    }

    @RequestMapping("/mail_input_message")
    public ModelAndView mailInputMessage() {
        return new ModelAndView("user/mail_input_message");
    }

    @RequestMapping("/success_password")
    public ModelAndView successPassword() {
        return new ModelAndView("user/success_password");
    }

    @RequestMapping("/profile")
    public ModelAndView profileHome(RedirectAttributes redirectAttributes,
                                    @RequestParam(name = "page", defaultValue = "0") int currentPage, @ModelAttribute("loggedUser") User loggeduser) {
        ModelAndView mav = new ModelAndView("profile/profile_home");

        mav.addObject("loggedUser", loggeduser);
        mav.addObject("locationsUser", loggeduser.getUserLocations());
        mav.addObject("reviews", userReviewService.getReviewsEarnedByUserId(loggeduser.getUserId(), currentPage));
        mav.addObject("userRating", userReviewService.getUserRatingEarned(loggeduser.getUserId()));

        return mav;
    }

    @RequestMapping("/language")
    public ModelAndView changeLanguage(@RequestParam(name = "lang") String lang, HttpServletRequest request) {
    	
        Locale locale = Locale.forLanguageTag(lang);
        LocaleContextHolder.setLocale(locale);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {

            try {
                us.setUserLanguage(pud.getUser(), lang);
            } catch (ApplicationRuntimeException e) {
                LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
                return new ModelAndView("redirect:/400");
            }
        }

        return new ModelAndView("redirect:/profile");
    }
    
    @PostMapping("/user/addLocation")
    public ModelAndView addLocation(@RequestParam Long userId, @RequestParam String locationString) {

        try {
            us.addLocation(userId, locationString);
        } catch (Exception e){
            ModelAndView errormav = new ModelAndView("/debug");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();

            errormav.addObject("error", stackTrace);
            return errormav;

        }

		User updatedUser = us.findById(userId);
		ModelAndView modelAndView = new ModelAndView("redirect:/profile");
		modelAndView.addObject("loggedUser", updatedUser);
		
		return modelAndView;
    }

    @PostMapping("/user/removeLocation")
    public ModelAndView removeLocation(@RequestParam Long userId, @RequestParam Long locationId) {
    	
        us.removeLocation(userId, locationId);
        
		User updatedUser = us.findById(userId);
		ModelAndView modelAndView = new ModelAndView("redirect:/profile");
		modelAndView.addObject("loggedUser", updatedUser);
		
		return modelAndView;
    }
}