package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final EmailService emailService;
    private final UserService userService;
    private final PublicationService publicationService;
    private final BookService bookService;
    private final BookModelService bookModelService;

    public ExchangeController(final ExchangeService exchangeService, final EmailService emailService, final UserService userService, final BookService bookService, final PublicationService publicationService, BookModelService bookModelService) {
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.userService = userService;
        this.bookService = bookService;
        this.publicationService = publicationService;
        this.bookModelService = bookModelService;
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

    @RequestMapping(path = "/exchange/initializeexchange", method = RequestMethod.POST)
   // public ModelAndView createExchange(@RequestParam (name = "publication_id") long offererPubId, @RequestParam (name = "bookId") long bookId){
    public ModelAndView createExchange(@ModelAttribute("completeBookParam") CompleteBook completeBook, @RequestParam("publication_id") long publicationId){
        
        final ModelAndView mav = new ModelAndView("exchange/exchange_initialized_confirmation");

        exchangeService.initializeExchange(completeBook, publicationId);

        return mav;
    }


    @RequestMapping("/exchange/accepted")
    public ModelAndView exchangeAccepted(@RequestParam long acceptCode) {
        final ModelAndView mav = new ModelAndView("exchange/exchange_accepted");

        return mav;
    }

    @RequestMapping("/exchange/invalid")
    public ModelAndView exchangeRejected() {

        return new ModelAndView("/exchange/invalid");
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

        String offererEmail = offerer.getMail();
        String requesterEmail = requester.getMail();

        BookModel offeredBookModel = bookModelService.getBookModelByBookModelId(bookOffered.getBookModelId());
        BookModel requestedBookModel = bookModelService.getBookModelByBookModelId(bookRequested.getBookModelId());

        variables.put("requestedBook", requestedBookModel.getTitle());
        variables.put("offeredBook", offeredBookModel.getTitle());

        variables.put("requesterEmail", requesterEmail);
        variables.put("requesterName", requester.getUsername());

        //variables.put("requesterEmail", offererEmail);    ¡??????
        //variables.put("requesterName", offerer.getUsername());

        variables.put("offererName", offerer.getUsername());
        variables.put("offererEmail", offererEmail);

        emailService.sendExchangeEmail(requesterEmail, variables, state);

        return mav;
    }
}
