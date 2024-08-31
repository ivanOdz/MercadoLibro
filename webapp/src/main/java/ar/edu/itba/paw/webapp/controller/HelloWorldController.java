package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
        mav.addObject("userId", userId);
        return mav;
    }


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