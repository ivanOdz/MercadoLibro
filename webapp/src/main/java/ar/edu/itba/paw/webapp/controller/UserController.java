package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.form.PasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.validation.Valid;


@Controller
public class UserController {
    private final UserService us;

    private AuthenticationManager auth;

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
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
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

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("userForm") UserForm userForm){
        return new ModelAndView("user/create");
    }

    @RequestMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("user/login");
    }


    @RequestMapping("/verification")
    public ModelAndView verificationController(@RequestParam(name = "verification_code") int verificationCode){
        us.verifyUser(verificationCode);
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
            System.out.print(errors.getAllErrors());
            return createPasswordForm(passwordForm, verificationCode);
        }
        us.changePassword(verificationCode, passwordForm.getPassword() );
        return new ModelAndView("redirect:/success_password");
    }


    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult errors) {

        if (errors.hasErrors()){
            return createForm(userForm);
        }

        // verify date, create an user and send a verification email
        final User user = us.createUser(userForm.getUsername(), userForm.getMail(), userForm.getPassword());
//
//        try {
//            // create a session and keep the user logged in
//            final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userForm.getUsername(), userForm.getPassword(), null);
//            SecurityContextHolder.getContext().setAuthentication(auth.authenticate(authenticationToken));
//        }catch(Exception e) {
//            System.out.println(e.getMessage());
//        }

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
            return pud.getUser();
        }
        return null;
    }


    @RequestMapping("/profile")
    public ModelAndView profileHome() {
        ModelAndView mav = new ModelAndView("profile/profile_home");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            mav.addObject("loggedUser", pud.getUser());
            mav.addObject("reviews", us.getReviewsByUserId(pud.getUser().getUserId()));
        }

        return mav;
    }
}