package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.interfaces.services.EmailService;
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

import java.util.HashMap;
import java.util.Map;


@Controller
public class HelloWorldController {

    private final UserService us;

    private final EmailService emailService;

    private AuthenticationManager auth;

    public HelloWorldController(final UserService us, final EmailService emailService, AuthenticationManager auth) {
        this.us = us;
        this.emailService = emailService;
        this.auth = auth;
    }

    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(name = "userId", defaultValue = "1") long userId) {
        final ModelAndView mav = new ModelAndView("helloworld/index");
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
        final ModelAndView mav = new ModelAndView("helloworld/profile");
        mav.addObject("username", us.findById(userId).get().getUsername());
        mav.addObject("mail", us.findById(userId).get());
        mav.addObject("userId", userId);
        return mav;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult errors) {

        if (errors.hasErrors()){
            System.out.println(errors.getAllErrors());
            return createForm(userForm);
        }
        final User user = us.createUser(userForm.getUsername(), userForm.getMail(), userForm.getPassword());

        // Creamos una sesión y dejamos logeado al usuario
        final Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userForm.getUsername(), userForm.getPassword(), null);
        SecurityContextHolder.getContext().setAuthentication(auth.authenticate(authenticationToken));

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("userForm") UserForm userForm){
        return new ModelAndView("helloworld/create");
    }

    @RequestMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("helloworld/login");
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

    // home
    @RequestMapping("/mail")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/home");
        Map<String, Object> variables = new HashMap<>();
        variables.put("requesterName", "Julieta Techenski");
        variables.put("requesterEmail", "mtaurian@gmail.com");
        variables.put("requestedPublication", "Deutsch Kursbuch");
        variables.put("offeredPublication", "Harry Potter 1");
        variables.put("rejectionUrl", "http://localhost:8080/publication?publication_id=3");
        variables.put("validationUrl", "http://localhost:8080/publication?publication_id=3");
        emailService.sendEmail("modzomek@itba.edu.ar", variables, "exchangeRequest", "Book Exchange");
        return mav;
    }
}