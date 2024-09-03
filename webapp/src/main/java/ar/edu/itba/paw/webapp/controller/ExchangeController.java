package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ExchangeController {

    ExchangeService exchangeService;
    EmailService emailService;
    UserService userService;
    PublicationsService publicationsService;
    BookService bookService;

    public ExchangeController(final ExchangeService exchangeService, final EmailService emailService, final UserService userService, final BookService bookService, final PublicationsService publicationsService) {
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.userService = userService;
        this.bookService = bookService;
        this.publicationsService = publicationsService;
    }

    @RequestMapping("/exchange")
    public ModelAndView exchange(@RequestParam(name = "acceptCode") long acceptCode, @RequestParam(name = "state") boolean state) {
        final ModelAndView mav = new ModelAndView(exchangeService.exchange(acceptCode, state));

        Map<String, Object> variables = new HashMap<>();
        long exchangeId = exchangeService.getId(acceptCode);

        Exchange exchange = exchangeService.getExchangeById(exchangeId).get();
        Publication offererPub = publicationsService.getPublicationById(exchange.getOfferer()).get();
        Publication requesterPub = publicationsService.getPublicationById(exchange.getRequester()).get();

        Book bookOffered = bookService.getBookById(offererPub.getBookId()).get();
        Book bookRequested = bookService.getBookById(requesterPub.getBookId()).get();

        User requester = userService.findById(requesterPub.getUserId()).get();

        String requesterEmail = requester.getMail();

        variables.put("requesterEmail", requesterEmail);
        variables.put("requesterName", requester.getUsername());
        variables.put("requestedBook", bookRequested.getDescription());
        variables.put("offeredBook", bookOffered.getDescription());

        emailService.sendExchangeEmail(requesterEmail, variables, state);

        return mav;
    }
}
