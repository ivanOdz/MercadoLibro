package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.form.MailForm;
import ar.edu.itba.paw.webapp.form.PasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import java.util.Locale;


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


    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("user/login");

        if (error != null) {
            Locale locale = LocaleContextHolder.getLocale();

            String errorMessage = messageSource.getMessage("login.invalid", null, locale);
            modelAndView.addObject("error", errorMessage);
        }

        return modelAndView;
    }

    @RequestMapping("/verification")
    public ModelAndView verificationController(@RequestParam(name = "verification_code") int verificationCode) {
        User user = us.getUserToVerify(verificationCode);
        us.verifyUser(verificationCode);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return new ModelAndView("redirect:/success_verification");
    }

    @RequestMapping("/check_verify")
    public ModelAndView checkVerify(@ModelAttribute("loggedUser") User loggeduser) {
        if (loggeduser.isVerified()) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/logout");
    }

    @RequestMapping(path = "/mail_input", method = RequestMethod.GET)
    public ModelAndView mailInput(@ModelAttribute("mailForm") MailForm mailForm) {
        ModelAndView modelAndView = new ModelAndView("user/mail_input");
        modelAndView.addObject("mailForm", new MailForm());
        return modelAndView;
    }

    @RequestMapping(path = "/mail_input", method = RequestMethod.POST)
    public ModelAndView mailInputCheck(@Valid @ModelAttribute("mailForm") MailForm mailForm, BindingResult errors) {
        if(errors.hasErrors()) {
            return new ModelAndView("user/mail_input"); // Devuelve la vista explícitamente para mantener BindingResult
        }
        us.changePasswordSolicited(mailForm.getEmail());

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
            return createPasswordForm(passwordForm, verificationCode);
        }
        us.changePassword(verificationCode, passwordForm.getPassword());
        User user = us.getUserToVerify(verificationCode);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return new ModelAndView("redirect:/success_password");
    }

    @PostMapping(value = "/changeUsername")
    public String changeUsername(@RequestParam("loggedUserId") long userId, @RequestParam("newUsername") String newUsername, RedirectAttributes redirectAttributes) {
        boolean updated = us.changeUserName(userId, newUsername);
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
    public ModelAndView create(@Valid @ModelAttribute("userForm") UserForm userForm,
                               BindingResult errors) {

        if (errors.hasErrors()) {
            return createForm(userForm);
        }

        if (us.userExists(userForm.getMail())) {
            errors.rejectValue("mail", "error.user.exists");
            return createForm(userForm);
        }

        us.createUser(userForm.getUsername(), userForm.getMail(), userForm.getPassword(), LocaleContextHolder.getLocale().toLanguageTag());
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
    public ModelAndView profileHome(@RequestParam(name = "page", defaultValue = "0") int currentPage, @ModelAttribute("loggedUser") User loggeduser) {
        ModelAndView mav = new ModelAndView("profile/profile_home");

        mav.addObject("locationsUser", loggeduser.getUserLocations());
        mav.addObject("reviews", userReviewService.getReviewsEarnedByUserId(loggeduser.getUserId(), currentPage));
        mav.addObject("userRating", userReviewService.getUserRatingEarned(loggeduser.getUserId()));

        return mav;
    }

    @RequestMapping("/language")
    public ModelAndView changeLanguage(@RequestParam(name = "lang") String lang,  @ModelAttribute("loggedUser") User loggeduser) {
        Locale locale = Locale.forLanguageTag(lang);
        LocaleContextHolder.setLocale(locale);
        us.setUserLanguage(loggeduser, lang);

        return new ModelAndView("redirect:/profile");
    }

    @PostMapping("/user/addLocation")
    public ModelAndView addLocation(@RequestParam Long userId, @RequestParam String locationString) {
        us.addLocation(userId, locationString);

		return new ModelAndView("redirect:/profile");
    }

    @PostMapping("/user/removeLocation")
    public ModelAndView removeLocation(@RequestParam Long userId, @RequestParam Long locationId) {
        us.removeLocation(userId, locationId);

		return new ModelAndView("redirect:/profile");
    }
}