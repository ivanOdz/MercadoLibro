package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.services.EmailService;
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

    public HelloWorldController(final UserService us, final EmailService emailService) {
        this.us = us;
        this.emailService = emailService;
    }

    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(name = "userId", defaultValue = "1") long userId) {
        final ModelAndView mav = new ModelAndView("helloworld/index");
        mav.addObject("username", us.findById(userId).get().getUsername());
        mav.addObject("userId", userId);
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
            return createForm(userForm);
        }
        final User user = us.create(userForm.getUsername(), userForm.getMail());
        return new ModelAndView("redirect:/" + user.getId());
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("userForm") UserForm userForm){
        return new ModelAndView("helloworld/create");
    }

    // home
    @RequestMapping("/mail")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/home");
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", "Julieta");
        variables.put("signUpDate", "August 31, 2024");
        emailService.sendEmail("jtechenski@itba.edu.ar", variables, "welcome");
        return mav;
    }

}