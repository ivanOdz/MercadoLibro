package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Timestamp;
import java.sql.Date;
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
    @Autowired
    private final UserReviewService userReviewService;
    
    public ExchangeController(final ExchangeService exchangeService, final EmailService emailService, final UserService userService, final BookService bookService, final PublicationService publicationService, BookModelService bookModelService, UserReviewService userReviewService) {
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.userService = userService;
        this.bookService = bookService;
        this.publicationService = publicationService;
        this.bookModelService = bookModelService;
        this.userReviewService = userReviewService;
    }

    // Requests (osea peticiones que me hacen a mi)
    // Paso el ID, y quiero aquellas exchanges en las que soy offerer
    @RequestMapping("/offers")
    public ModelAndView exchangeRequests(@RequestParam(name = "exchange-state", defaultValue = "PENDING") ExchangeState exchangeState) {
        final ModelAndView mav = new ModelAndView("exchange/exchange_requests");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {

            List<Exchange> exchangeWrapperList = exchangeService.getExchangeOffererListByUserId(pud.getUser().getUserId(), exchangeState);
            mav.addObject("exchangeState", exchangeState);
            mav.addObject("exchanges", exchangeWrapperList);
            mav.addObject("review", new UserReview());
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
        }

        return mav;
    }


    // Estado de mis ofertas
    // Paso el ID, y quiero aquellas exchanges en las que soy requester
    @RequestMapping(path="/requests", method= RequestMethod.GET)
    public ModelAndView exchangeOffers(@RequestParam(name = "exchange-state", defaultValue = "PENDING") ExchangeState exchangeState) {   // TODO: VALOR DE EXCHANGEsTATE
        final ModelAndView mav = new ModelAndView("exchange/exchange_offers");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            List<Exchange> exchangeWrapperList = exchangeService.getExchangeRequesterListByUserId(pud.getUser().getUserId(), exchangeState);
            //mav.addObject("exchanges", exchangeWrapperList);
            User loggedUser = userService.findById(pud.getUser().getUserId()).get();
            mav.addObject("loggedUser", loggedUser);
            mav.addObject("review", new UserReview());
        }
        
        return mav;
    }

    /*@RequestMapping(path = "/exchange/initializeexchange", method = RequestMethod.POST)
    // public ModelAndView createExchange(@RequestParam (name = "publication_id") long offererPubId, @RequestParam (name = "bookId") long bookId){
    public ModelAndView createExchange(@ModelAttribute("completeBookParam") CompleteBook completeBook, @RequestParam("publication_id") long publicationId){

        final ModelAndView mav = new ModelAndView("exchange/exchange_initialized_confirmation");

        exchangeService.initializeExchange(completeBook, publicationId);

        return mav;
    }*/


    @RequestMapping("/exchange/accepted")
    public ModelAndView exchangeAccepted(@RequestParam long acceptCode) {
        final ModelAndView mav = new ModelAndView("exchange/exchange_accepted");

        return mav;
    }

    @RequestMapping("/exchange/invalid")
    public ModelAndView exchangeRejected() {
        return new ModelAndView("/exchange/invalid");
    }

    /*@RequestMapping("/createexchange")
    public ModelAndView exchange(@RequestParam(name = "accept_code") int acceptCode, @RequestParam(name = "state") boolean state) {
        ModelAndView mav = new ModelAndView("error/failed_authentication");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // if the user that is accepting/rejecting the exchange is the one that should
        if (authentication.getPrincipal() instanceof PawUserDetails pud
            && publicationService.getPublicationById(exchangeService.getExchangeById(exchangeService.getId(acceptCode)).get().getOffererPubId()).get().getUserId() == pud.getUser().getUserId()){
        mav = new ModelAndView(exchangeService.exchange(acceptCode, state));

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

        variables.put("offererName", offerer.getUsername());
        variables.put("offererEmail", offererEmail);

        emailService.sendExchangeEmail(requesterEmail, variables, state);
        }

        return mav;
    }*/


    @RequestMapping("/confirm_offerer")
    public ModelAndView confirmExchangeOffer(@RequestParam(name = "accept_code") int accept_code) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // TODO: error management of whether its null
        Exchange exchange = exchangeService.getExchangeByAcceptCode(accept_code).get();

        // if the user that is accepting/rejecting the exchange is the one that should
        if (authentication.getPrincipal() instanceof PawUserDetails pud
                && exchange.getOfferer().getBook().getOwner().getUserId() == pud.getUser().getUserId()) {
            exchangeService.cofirmOfferer(accept_code);
            return exchangeRequests(exchange.getExchangeState());
        }

        return new ModelAndView("redirect:/failed_authentication");
    }

    @RequestMapping("/failed_authentication")
    public ModelAndView failedAuthentication() {
        return new ModelAndView("error/failed_authentication");
    }


    @RequestMapping("/confirm_requester")
    public ModelAndView confirmExchangeRequest(@RequestParam(name = "accept_code") int accept_code) {
        ModelAndView mav = new ModelAndView("error/failed_authentication");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // TODO: error management of whether its null
        Exchange exchange = exchangeService.getExchangeByAcceptCode(accept_code).get();

        // if the user that is accepting/rejecting the exchange is the one that should
        if (authentication.getPrincipal() instanceof PawUserDetails pud
                && exchange.getRequester().getBook().getOwner().getUserId() == pud.getUser().getUserId()) {
            exchangeService.cofirmRequester(accept_code);
            return exchangeRequests(exchange.getExchangeState());
        }

        return mav;
    }

    @RequestMapping(path = "/submitReview", method = RequestMethod.POST)
    public ModelAndView submitReview(
		@RequestParam("exchangeId") long exchangeId,
		@RequestParam("reviewerId") long reviewerId,
		@RequestParam("subjectId") long subjectId,
		@RequestParam("reviewDescription") String reviewDescription,
		@RequestParam("userReviewRating") int userReviewRating/*,
		BindingResult result, RedirectAttributes redirectAttributes*/) {
		
		UserReview userReview = new UserReview((long)0, exchangeId, reviewerId, subjectId, reviewDescription, new java.sql.Timestamp(0), userReviewRating);

		boolean success = userReviewService.createUserReview(userReview);

        /*
        if (success) {
            return new ModelAndView("redirect:/successPage");
        } else {
            return new ModelAndView("redirect:/errorPage");
        }*/
        
        return new ModelAndView("redirect:/requests");
    }
}
