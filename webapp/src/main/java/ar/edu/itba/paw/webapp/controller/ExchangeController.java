package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Publication;
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

    public ExchangeController(final ExchangeService exchangeService, final EmailService emailService, final UserService userService, final BookService bookService){
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @RequestMapping("/rejectExchange/{acceptCode:\\d+}")
    public ModelAndView rejectExchange(@PathVariable(name = "acceptCode") long acceptCode) {
        final ModelAndView mav = new ModelAndView("exchange/rejected");
        exchangeService.rejectExchange(acceptCode);

        Map<String, Object> variables = new HashMap<>();
        int exchangeId = exchangeService.getId(acceptCode);

        Exchange exchange = exchangeService.getExchangeById(exchangeId);
        Publication offerer = publicationsService.getPublicationById(exchange.getOfferer());
        Publication requester = publicationsService.getPublicationById(exchange.getRequester());

        Book bookOffered = bookService.getBookById(offerer.getBookId());
        Book bookRequested = bookService.getBookById(requester.getBookId());



        variables.put("requesterEmail", exchangeService.getRequesterEmail(acceptCode));
        variables.put("publicationName", bookOffered.getDescription());
//        variables.put("validationUrl", "http://localhost:8080/publication?publicationId=3");
//        variables.put("username", "Julieta");
//        variables.put("signUpDate", "August 31, 2024");
        emailService.sendEmail("jtechenski@itba.edu.ar", variables, "exchangeRequest", "Book Exchange");


//         ms.sendRejectedExchange(); // al usuario que le rechazaron el intercambio

        mav.addObject("exchangeId", acceptCode);
        return mav;
    }

    @RequestMapping("/acceptExchange/{acceptCode:\\d+}")
    public ModelAndView acceptExchange(@PathVariable(name = "acceptCode") long acceptCode) {
        final ModelAndView mav = new ModelAndView("exchange/accepted");

         exchangeService.acceptExchange(acceptCode);
//         publicationsService.closePublication();
         // modificar estado de la publicacion -> mi duda es si defino una varibale que sea PublicationDao o como sería lo "correcto"



//         ms.sendEmail(); // enviamos mail al usuario al que le acceptaron el intercambio
        // ???? enviamos tmb mail a los usuarios que habian "aplicado" para el intercambio

        mav.addObject("exchangeId", acceptCode);
        return mav;
    }


}
