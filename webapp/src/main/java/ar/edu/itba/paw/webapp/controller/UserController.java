package ar.edu.itba.paw.webapp.controller;

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
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;


@Controller
public class UserController {
    private final UserService us;

    @Autowired
    private PawUserDetailsService userDetailsService;


    private AuthenticationManager auth;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserReviewService userReviewService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(final UserService us, AuthenticationManager auth) {
        this.us = us;
        this.auth = auth;
    }

    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(name = "userId", defaultValue = "1") long userId) {
        final ModelAndView mav = new ModelAndView("user/index");
        mav.addObject("username", us.findById(userId).get().getUsername());
        mav.addObject("userId", userId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            mav.addObject("loggedUser", pud.getUser());
        }
        return mav;
    }

    @RequestMapping("/{userId:\\d+}")
    public ModelAndView profile(@PathVariable(name = "userId") long userId) {
        final ModelAndView mav = new ModelAndView("user/profile");
        mav.addObject("username", us.findById(userId).get().getUsername());
        mav.addObject("mail", us.findById(userId).get());
        mav.addObject("userId", userId);
        return mav;
    }

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
    public ModelAndView verificationController(@RequestParam(name = "verification_code") int verificationCode){
//         obtengo la sesion activa
        User user = us.getUserToVerify(verificationCode).get();

        us.verifyUser(verificationCode);
        try {
            // create a session and keep the user logged in
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return new ModelAndView("redirect:/success_verification");
    }

    @RequestMapping("/check_verify")
    public ModelAndView checkVerify(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            if(pud.getUser().isVerified()){
                return new ModelAndView("redirect:/");
            }
        }
        return new ModelAndView("redirect:/logout");
    }

    @RequestMapping("/mail_input")
    public ModelAndView mailInput(){
        return new ModelAndView("user/mail_input");
    }

    @RequestMapping("/change_password_solicited")
    public ModelAndView changePasswordSolicited(@RequestParam(name = "email") String email){
        us.changePasswordSolicited(email);
        return new ModelAndView("redirect:/mail_input_message");
    }

    @RequestMapping(path = "/change_password", method = RequestMethod.GET)
    public ModelAndView createPasswordForm(@ModelAttribute("passwordForm") PasswordForm passwordForm, @RequestParam(name = "verification_code") int verificationCode){
        ModelAndView mav = new ModelAndView("user/new_password");
        mav.addObject("verification_code", verificationCode);
        return mav;
    }

    @RequestMapping(value = "/change_password", method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid @ModelAttribute("passwordForm") PasswordForm passwordForm, BindingResult errors, @RequestParam(name = "verification_code") int verificationCode){
        if(errors.hasErrors()){
           //System.out.print(errors.getAllErrors());
            return createPasswordForm(passwordForm, verificationCode);
        }
        us.changePassword(verificationCode, passwordForm.getPassword() );
        return new ModelAndView("redirect:/success_password");
    }

    @PostMapping(value = "/changeUsername")
    public String changeUsername(@RequestParam("loggedUserId") long userId, @RequestParam("newUsername") String newUsername, RedirectAttributes redirectAttributes) {

    	boolean updated = us.changeUserName(userId, newUsername);

    	if (updated) {
    		redirectAttributes.addFlashAttribute("message", "done");
    	}
    	else {
    		redirectAttributes.addFlashAttribute("errorMessage",  "failed");
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

        if (errors.hasErrors()){
            return createForm(userForm);
        }

        if(us.userExists(userForm.getMail())){
            errors.rejectValue("mail", "error.user.exists");
            return createForm(userForm);
        }

        // using browserLanguage as user default
        String browserLanguage = request.getHeader("Accept-Language");

        // verify date, create an user and send a verification email
        final User user = us.createUser(userForm.getUsername(), userForm.getMail(), userForm.getPassword(), browserLanguage.split(",")[0]);

        return new ModelAndView("redirect:/success_registration");
    }

    // success screens

    @RequestMapping( "/success_registration")
    public ModelAndView successRegistration(){
        return new ModelAndView("user/success_registration");
    }

    @RequestMapping( "/success_verification")
    public ModelAndView successVerification(){
        return new ModelAndView("user/success_verification");
    }

    @RequestMapping("/mail_input_message")
    public ModelAndView mailInputMessage(){
        return new ModelAndView("user/mail_input_message");
    }

    @RequestMapping( "/success_password")
    public ModelAndView successPassword(){
        return new ModelAndView("user/success_password");
    }

    // binding=false -> read only attribute
    @ModelAttribute(name="loggedUser", binding = false)
    public User getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud){
            LOGGER.debug("Logged user is {}", pud.getUser());
            return pud.getUser();
        }
        return null;
    }

    @RequestMapping("/profile")
    public ModelAndView profileHome(RedirectAttributes redirectAttributes,
                                    @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex) {

        ModelAndView mav = new ModelAndView("profile/profile_home");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Este es un mensaje de info");
        LOGGER.error("Este es un mensaje de error");
        LOGGER.debug("Este es un mensaje de debug");
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {

            User loggedUser = us.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
            mav.addObject("reviews", us.getReviewsByUserId(loggedUser.getUserId(), pageIndex));
            mav.addObject("userRating", us.getUserRating(loggedUser.getUserId()));
        }

        return mav;
    }

    @RequestMapping("/language")
    public ModelAndView changeLanguage(@RequestParam(name = "lang") String lang, HttpServletRequest request) {
        Locale locale = Locale.forLanguageTag(lang);
        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            us.setUserLanguage(pud.getUser(), lang);
        }

        return new ModelAndView("redirect:/");
    }
}