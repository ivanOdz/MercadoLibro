package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @RequestMapping("/rejectExchange/{acceptCode:\\d+}")
    public ModelAndView rejectExchange(@PathVariable(name = "acceptCode") long acceptCode) {
        final ModelAndView mav = new ModelAndView("exchange/rejected");
        exchangeService.rejectExchange(acceptCode);

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
        emailService.sendEmail(requesterEmail, variables, "exchangeRejected", "Book Exchange Rejected");

        mav.addObject("acceptCode", acceptCode);
        return mav;
    }

    @RequestMapping("/acceptExchange/{acceptCode:\\d+}")
    public ModelAndView acceptExchange(@PathVariable(name = "acceptCode") long acceptCode) {
        final ModelAndView mav = new ModelAndView("exchange/accepted");
        exchangeService.acceptExchange(acceptCode);


        // TODO: Actualizar dueño del libro
        // TODO: Rechazar todas las peticiones del mismo libro

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
        emailService.sendEmail(requesterEmail, variables, "exchangeAccepted", "Book Exchange Accepted");

        mav.addObject("acceptCode", acceptCode);
        return mav;
    }


}
