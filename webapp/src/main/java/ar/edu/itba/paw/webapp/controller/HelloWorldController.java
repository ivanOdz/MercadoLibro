package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Qualifier;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class HelloWorldController {

    private UserService us;

    @Autowired
    private EmailService emailService;

    public HelloWorldController(final UserService us) {
        this.us = us;
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
        emailService.sendEmail("jtechenski@itba.edu.ar");
        return mav;
    }
}