package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;

import ar.edu.itba.paw.webapp.form.ExchangeForm;
import ar.edu.itba.paw.webapp.form.UserReviewForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Controller
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final PublicationService publicationService;
    private final BookService bookService;


    @Autowired
    private final UserReviewService userReviewService;

    @Autowired
    private LoggedUserAdvice loggedUserAdvice;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeController.class);


    public ExchangeController(final ExchangeService exchangeService, PublicationService publicationService, BookService bookService, UserReviewService userReviewService) {
        this.exchangeService = exchangeService;
        this.publicationService = publicationService;
        this.bookService = bookService;
        this.userReviewService = userReviewService;
    }

    // Requests (osea peticiones que me hacen a mi)
    // Paso el ID, y quiero aquellas exchanges en las que soy offerer
    @RequestMapping("/offers")
    public ModelAndView exchangeRequests(@RequestParam(name = "pending-page", defaultValue = "0") int pendingPage,
                                        @RequestParam(name = "in-progress-page", defaultValue = "0") int inProgressPage,
                                        @RequestParam(name = "completed-page", defaultValue = "0") int completedPage,
                                        @RequestParam(name = "rejected-page", defaultValue = "0") int rejectedPage) {
        final ModelAndView mav = new ModelAndView("exchange/exchange_requests");

        User user = loggedUserAdvice.getLoggedUser();
        PaginatedResponse<Exchange, BasicMetadata> pendingExchanges = exchangeService.getExchangeOffererListByUserId(user.getUserId(), pendingPage, ExchangeState.PENDING);
        PaginatedResponse<Exchange, BasicMetadata> inProcessExchanges = exchangeService.getExchangeOffererListByUserId(user.getUserId(), inProgressPage, ExchangeState.ACCEPTED);
        PaginatedResponse<Exchange, BasicMetadata> completedExchanges = exchangeService.getExchangeOffererListByUserId(user.getUserId(), completedPage, ExchangeState.TERMINATED);
        PaginatedResponse<Exchange, BasicMetadata> rejectedExchanges = exchangeService.getExchangeOffererListByUserId(user.getUserId(), rejectedPage, ExchangeState.REJECTED);

        mav.addObject("pending", pendingExchanges);
        mav.addObject("inProgress", inProcessExchanges);
        mav.addObject("completed", completedExchanges);
        mav.addObject("rejected", rejectedExchanges);
        mav.addObject("userReviewForm", new UserReviewForm());

        return mav;
    }


    // Estado de mis ofertas
    // Paso el ID, y quiero aquellas exchanges en las que soy requester
    @RequestMapping(path = "/requests", method = RequestMethod.GET)
    public ModelAndView exchangeOffers(@RequestParam(name = "pending-page", defaultValue = "0") int pendingPage,
                                       @RequestParam(name = "in-progress-page", defaultValue = "0") int inProgressPage,
                                       @RequestParam(name = "completed-page", defaultValue = "0") int completedPage,
                                       @RequestParam(name = "rejected-page", defaultValue = "0") int rejectedPage) {
        final ModelAndView mav = new ModelAndView("exchange/exchange_offers");

        User user = loggedUserAdvice.getLoggedUser();
        PaginatedResponse<Exchange, BasicMetadata> pendingExchanges = exchangeService.getExchangeRequesterListByUserId(user.getUserId(), pendingPage, ExchangeState.PENDING);
        PaginatedResponse<Exchange, BasicMetadata> inProcessExchanges = exchangeService.getExchangeRequesterListByUserId(user.getUserId(), inProgressPage, ExchangeState.ACCEPTED);
        PaginatedResponse<Exchange, BasicMetadata> completedExchanges = exchangeService.getExchangeRequesterListByUserId(user.getUserId(), completedPage,ExchangeState.TERMINATED);
        PaginatedResponse<Exchange, BasicMetadata> rejectedExchanges = exchangeService.getExchangeRequesterListByUserId(user.getUserId(), rejectedPage, ExchangeState.REJECTED);

        mav.addObject("pending", pendingExchanges);
        mav.addObject("inProgress", inProcessExchanges);
        mav.addObject("completed", completedExchanges);
        mav.addObject("rejected", rejectedExchanges);
        mav.addObject("userReviewForm", new UserReviewForm());

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
        ModelAndView mav = new ModelAndView("error/failed_authentication");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Exchange> ex = exchangeService.getExchangeByAcceptCode(acceptCode);

        // if the user that is accepting/rejecting the exchange is the one that should
        if (authentication.getPrincipal() instanceof PawUserDetails pud &&
                ex.get().getOfferer().getBook().getOwner().getUserId() == pud.getUser().getUserId()) {
            mav = new ModelAndView(exchangeService.exchange(acceptCode, state));
        }

        return mav;
    }


    @RequestMapping("/confirm_offerer")
    public ModelAndView confirmExchangeOffer(@RequestParam(name = "accept_code") int accept_code) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // TODO: error management of whether its null
        Exchange exchange = exchangeService.getExchangeByAcceptCode(accept_code).get();

        // if the user that is accepting/rejecting the exchange is the one that should
        if (authentication.getPrincipal() instanceof PawUserDetails pud
                && exchange.getOfferer().getBook().getOwner().getUserId() == pud.getUser().getUserId()) {
            exchangeService.cofirmOfferer(accept_code);
            return new ModelAndView("redirect:/offers");
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
            return new ModelAndView("redirect:/requests");
        }

        return mav;
    }

    @GetMapping("/start_exchange")
    public ModelAndView startExchange(@ModelAttribute("exchangeForm") ExchangeForm exchangeForm, BindingResult errors, @RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("/exchange/solicit_exchange");
        Publication publication = publicationService.getPublicationByPublicationId(publicationId);
        List<Book> availableBooks;

        availableBooks = bookService.getAvailableBooksByUser(loggedUserAdvice.getLoggedUser());
        mav.addObject("availableBooks", availableBooks);

        mav.addObject("exchangeForm", exchangeForm);
        mav.addObject("publication", publication);

        return mav;
    }

    @PostMapping(path = "/exchange/initializeexchange")
    public ModelAndView initializeExchange(@Valid @ModelAttribute("exchangeForm") ExchangeForm exchangeForm, BindingResult errors) {
        if(errors.hasErrors()){
            startExchange(exchangeForm, errors, exchangeForm.getPublicationId());
        }
        // Insertar tupla de requester en publicacion con fecha actual y publicationState = 2 (OFFERER)
        exchangeService.initializeExchange(exchangeForm.getBookId(), exchangeForm.getLocation(), exchangeForm.getPublicationId(), loggedUserAdvice.getLoggedUser());
        return new ModelAndView("redirect:/requests");  // TOOD: se podría redirigir a una página de éxito
    }

    @RequestMapping(path = "/submitReview", method = RequestMethod.POST)
    public ModelAndView submitReview(
            @RequestParam("exchangeId") long exchangeId,
            @RequestParam("reviewDescription") String reviewDescription,
            @RequestParam("userReviewRating") int userReviewRating/*,
		BindingResult result, RedirectAttributes redirectAttributes*/) {
    
        boolean success = userReviewService.createUserReview(exchangeId, loggedUserAdvice.getLoggedUser().getUserId(), reviewDescription, userReviewRating);

        /*
        if (success) {
            return new ModelAndView("redirect:/successPage");
        } else {
            return new ModelAndView("redirect:/errorPage");
        }*/

        return new ModelAndView("redirect:/requests");
    }

}
