package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class BookController {


    @RequestMapping("/book")
    public ModelAndView bookHome() {
        return new ModelAndView("book/book_home");
    }


    @RequestMapping("/book/upload_new_book")
    public ModelAndView uploadNewBook() {


        return new ModelAndView("book/newbook");
    }
}
