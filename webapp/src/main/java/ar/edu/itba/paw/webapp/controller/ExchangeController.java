package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExchangeController {

    ExchangeService exchangeService;
    EmailService emailService;
    UserService userService;
    PublicationService publicationService;
    BookService bookService;

    public ExchangeController(final ExchangeService exchangeService, final EmailService emailService, final UserService userService, final BookService bookService, final PublicationService publicationService) {
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.userService = userService;
        this.bookService = bookService;
        this.publicationService = publicationService;
    }


    @RequestMapping("/exchange")
    public ModelAndView exchangeHome() {
        final ModelAndView mav = new ModelAndView("exchange/exchange_home");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof PawUserDetails pud) {
            List<ExchangeWrapper> exchangeWrapperList = exchangeService.getExchangeWrapperListByUserId(pud.getUser().getUserId());
            mav.addObject("exchangeWrapperList", exchangeWrapperList);
        }

        return mav;
    }

    @RequestMapping("/createexchange")
    public ModelAndView exchange(@RequestParam(name = "accept_code") int acceptCode, @RequestParam(name = "state") boolean state) {
        final ModelAndView mav = new ModelAndView(exchangeService.exchange(acceptCode, state));

        Map<String, Object> variables = new HashMap<>();
        long exchangeId = exchangeService.getId(acceptCode);

        Exchange exchange = exchangeService.getExchangeById(exchangeId).get();
        Publication offererPub = publicationService.getPublicationById(exchange.getOffererPubId()).get();
        Publication requesterPub = publicationService.getPublicationById(exchange.getRequesterPubId()).get();

        Book bookOffered = bookService.getBookById(offererPub.getBookId()).get();
        Book bookRequested = bookService.getBookById(requesterPub.getBookId()).get();

        User requester = userService.findById(requesterPub.getUserId()).get();
        User offerer = userService.findById(offererPub.getUserId()).get();

        String requesterEmail = requester.getMail();

        variables.put("requesterEmail", requesterEmail);
        variables.put("requesterName", requester.getUsername());
        //variables.put("requestedBook", bookRequested.getTitle());
        //variables.put("offeredBook", bookOffered.getTitle());
        variables.put("offererName", offerer.getUsername());
        variables.put("offererEmail", offerer.getMail());

        emailService.sendExchangeEmail(requesterEmail, variables, state);

        return mav;
    }
}
