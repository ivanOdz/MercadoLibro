package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import ar.edu.itba.paw.webapp.form.ExchangeForm;
import ar.edu.itba.paw.webapp.form.MessageForm;
import ar.edu.itba.paw.webapp.form.UserReviewForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@Controller
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserReviewService userReviewService;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeController.class);

    // Requests (osea peticiones que me hacen a mi)
    // Paso el ID, y quiero aquellas exchanges en las que soy offerer
    @RequestMapping("/offers")
    public ModelAndView exchangeRequests(@RequestParam(name = "pending-page", defaultValue = "0") int pendingPage,
                                         @RequestParam(name = "in-progress-page", defaultValue = "0") int inProgressPage,
                                         @RequestParam(name = "completed-page", defaultValue = "0") int completedPage,
                                         @RequestParam(name = "rejected-page", defaultValue = "0") int rejectedPage,
                                         @ModelAttribute("loggedUser") User loggeduser) {
        final ModelAndView mav = new ModelAndView("exchange/exchange_requests");

        PaginatedResponse<Exchange, BasicMetadata> pendingExchanges = exchangeService.getExchangeOffererListByUserId(loggeduser.getUserId(), pendingPage, ExchangeState.PENDING);
        PaginatedResponse<Exchange, BasicMetadata> inProcessExchanges = exchangeService.getExchangeOffererListByUserId(loggeduser.getUserId(), inProgressPage, ExchangeState.ACCEPTED);
        PaginatedResponse<Exchange, BasicMetadata> completedExchanges = exchangeService.getExchangeOffererListByUserId(loggeduser.getUserId(), completedPage, ExchangeState.TERMINATED);
        PaginatedResponse<Exchange, BasicMetadata> rejectedExchanges = exchangeService.getExchangeOffererListByUserId(loggeduser.getUserId(), rejectedPage, ExchangeState.REJECTED);

        mav.addObject("pending", pendingExchanges);
        mav.addObject("inProgress", inProcessExchanges);
        mav.addObject("completed", completedExchanges);
        mav.addObject("rejected", rejectedExchanges);

        mav.addObject("userReviewForm", new UserReviewForm());

        mav.addObject("messageForm", new MessageForm());
        mav.addObject("messages", inProcessExchanges.getData().stream().map(Exchange::getChat).findFirst().orElse(Collections.emptyList()));

        List<Exchange> exchanges = new ArrayList<>(pendingExchanges.getData());
        exchanges.addAll(inProcessExchanges.getData());
        exchanges.addAll(completedExchanges.getData());
        exchanges.addAll(rejectedExchanges.getData());

        mav.addObject("exchanges",exchanges);

        return mav;
    }

    // Estado de mis ofertas
    // Paso el ID, y quiero aquellas exchanges en las que soy requester
    @RequestMapping(path = "/requests", method = RequestMethod.GET)
    public ModelAndView exchangeOffers(@RequestParam(name = "pending-page", defaultValue = "0") int pendingPage,
                                       @RequestParam(name = "in-progress-page", defaultValue = "0") int inProgressPage,
                                       @RequestParam(name = "completed-page", defaultValue = "0") int completedPage,
                                       @RequestParam(name = "rejected-page", defaultValue = "0") int rejectedPage,
                                       @ModelAttribute("loggedUser") User loggeduser) {
        final ModelAndView mav = new ModelAndView("exchange/exchange_offers");

        PaginatedResponse<Exchange, BasicMetadata> pendingExchanges = exchangeService.getExchangeRequesterListByUserId(loggeduser.getUserId(), pendingPage, ExchangeState.PENDING);
        PaginatedResponse<Exchange, BasicMetadata> inProcessExchanges = exchangeService.getExchangeRequesterListByUserId(loggeduser.getUserId(), inProgressPage, ExchangeState.ACCEPTED);
        PaginatedResponse<Exchange, BasicMetadata> completedExchanges = exchangeService.getExchangeRequesterListByUserId(loggeduser.getUserId(), completedPage, ExchangeState.TERMINATED);
        PaginatedResponse<Exchange, BasicMetadata> rejectedExchanges = exchangeService.getExchangeRequesterListByUserId(loggeduser.getUserId(), rejectedPage, ExchangeState.REJECTED);

        mav.addObject("pending", pendingExchanges);
        mav.addObject("inProgress", inProcessExchanges);
        mav.addObject("completed", completedExchanges);
        mav.addObject("rejected", rejectedExchanges);
        mav.addObject("userReviewForm", new UserReviewForm());

        mav.addObject("userReviewForm", new UserReviewForm());

        mav.addObject("messageForm", new MessageForm());
        mav.addObject("messages", inProcessExchanges.getData().stream().map(Exchange::getChat).findFirst().orElse(Collections.emptyList()));

        List<Exchange> exchanges = new ArrayList<>(pendingExchanges.getData());
        exchanges.addAll(inProcessExchanges.getData());
        exchanges.addAll(completedExchanges.getData());
        exchanges.addAll(rejectedExchanges.getData());

        mav.addObject("exchanges",exchanges);

        return mav;
    }

    @RequestMapping("/createexchange")
    public ModelAndView exchange(@RequestParam(name = "accept_code") int acceptCode, @RequestParam(name = "state") boolean state, @ModelAttribute("loggedUser") User loggeduser) {
        ModelAndView mav = new ModelAndView("error/failed_authentication");

        Exchange ex = exchangeService.getExchangeByAcceptCode(acceptCode);

        // if the user that is accepting/rejecting the exchange is the one that should
        if (ex.getOfferer().getBook().getOwner().getUserId() == loggeduser.getUserId()) {
            String exchangeView;
            exchangeView = exchangeService.exchange(acceptCode, state);
            mav = new ModelAndView(exchangeView);
        }
        LOGGER.info("Exchange started between {} and {}", ex.getOfferer().getBook().getOwner().getUsername(), ex.getRequester().getBook().getOwner().getUsername());
        return mav;
    }


    @RequestMapping("/exchange/accepted")
    public ModelAndView exchangeAccepted() {
        LOGGER.info(messageSource.getMessage("info.exchange.accepted", null, LocaleContextHolder.getLocale()));
        return new ModelAndView("exchange/accepted");
    }

    @RequestMapping("/exchange/invalid")
    public ModelAndView exchangeRejected() {
        LOGGER.info(messageSource.getMessage("info.exchange.rejected", null, LocaleContextHolder.getLocale()));
        return new ModelAndView("/exchange/invalid");
    }


    @GetMapping("/start_exchange")
    public ModelAndView startExchange(@ModelAttribute("exchangeForm") ExchangeForm exchangeForm, BindingResult errors, @RequestParam(name = "publication_id") long publicationId, @ModelAttribute("loggedUser") User loggeduser) {
    	
        final ModelAndView mav = new ModelAndView("/exchange/solicit_exchange");
        Publication publication;

        publication = publicationService.getPublicationByPublicationId(publicationId);

        List<Book> availableBooks;

        availableBooks = bookService.getAvailableBooksByUser(loggeduser);

        mav.addObject("user", loggeduser);
        mav.addObject("availableBooks", availableBooks);
        mav.addObject("exchangeForm", exchangeForm);
        mav.addObject("publication", publication);

        return mav;
    }

    @PostMapping(path = "/exchange/initializeexchange")
    public ModelAndView initializeExchange(@NotEmpty @Valid @ModelAttribute("exchangeForm") ExchangeForm exchangeInput, BindingResult errors, @ModelAttribute("loggedUser") User loggeduser) {
        if (errors.hasErrors()) {
            return startExchange(exchangeInput, errors, exchangeInput.getPublicationId(), loggeduser);
        }

        exchangeService.initializeExchange(exchangeInput.getBookId(), exchangeInput.getLocationId(), exchangeInput.getPublicationId());
        return new ModelAndView("redirect:/requests");
    }


    @RequestMapping("/confirm_offerer")
    public ModelAndView confirmExchangeOffer(@RequestParam(name = "accept_code") int accept_code, @ModelAttribute("loggedUser") User loggeduser) {
        Exchange exchange = exchangeService.getExchangeByAcceptCode(accept_code);

        // if the user that is accepting/rejecting the exchange is the one that should
        if (Objects.equals(exchange.getOfferer().getBook().getOwner().getUserId(), loggeduser.getUserId())) {
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
    public ModelAndView confirmExchangeRequest(@RequestParam(name = "accept_code") int accept_code, @ModelAttribute("loggedUser") User loggeduser) {
        Exchange exchange = exchangeService.getExchangeByAcceptCode(accept_code);

        // if the user that is accepting/rejecting the exchange is the one that should
        if (Objects.equals(exchange.getRequester().getBook().getOwner().getUserId(), loggeduser.getUserId())) {
            exchangeService.cofirmRequester(accept_code);
            return new ModelAndView("redirect:/requests");
        }
        return new ModelAndView("redirect:/failed_authentication");
    }

    @RequestMapping(path = "/submit_review", method = RequestMethod.POST)
    public ModelAndView submitReview(
            @RequestParam("exchangeId") long exchangeId,
            @RequestParam("reviewDescription") String reviewDescription,
            @RequestParam("userReviewRating") int userReviewRating,
            @ModelAttribute("loggedUser") User loggeduser) {

        Exchange e = exchangeService.getExchangeById(exchangeId);
        if (Objects.equals(e.getRequester().getBook().getOwner().getUserId(), loggeduser.getUserId()) || Objects.equals(e.getOfferer().getBook().getOwner().getUserId(), loggeduser.getUserId())) {
            userReviewService.createUserReview(exchangeId, loggeduser.getUserId(), reviewDescription, userReviewRating);
            return new ModelAndView("redirect:/requests");
        }
        return new ModelAndView("redirect:/failed_authentication");
    }

    @PostMapping( "/send_message")
    public void sendMessage(@RequestParam("chatExchangeId") long exchangeId,
                            @RequestParam("chatUserId") long userId,
                            @RequestParam("message") String message) {
            exchangeService.createMessage(exchangeId, userId, message);
    }

}
