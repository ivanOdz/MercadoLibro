package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.exceptions.base.ApplicationRuntimeException;
import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import ar.edu.itba.paw.webapp.form.ExchangeForm;
import ar.edu.itba.paw.webapp.form.UserReviewForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        PaginatedResponse<Exchange, BasicMetadata> completedExchanges = exchangeService.getExchangeRequesterListByUserId(user.getUserId(), completedPage, ExchangeState.TERMINATED);
        PaginatedResponse<Exchange, BasicMetadata> rejectedExchanges = exchangeService.getExchangeRequesterListByUserId(user.getUserId(), rejectedPage, ExchangeState.REJECTED);

        mav.addObject("pending", pendingExchanges);
        mav.addObject("inProgress", inProcessExchanges);
        mav.addObject("completed", completedExchanges);
        mav.addObject("rejected", rejectedExchanges);
        mav.addObject("userReviewForm", new UserReviewForm());

        return mav;
    }


    @RequestMapping("/exchange/accepted")
    public ModelAndView exchangeAccepted() {
        return new ModelAndView("exchange/accepted");
    }

    @RequestMapping("/exchange/invalid")
    public ModelAndView exchangeRejected() {
        return new ModelAndView("/exchange/invalid");
    }

    @RequestMapping("/createexchange")
    public ModelAndView exchange(@RequestParam(name = "accept_code") int acceptCode, @RequestParam(name = "state") boolean state) {
        ModelAndView mav = new ModelAndView("error/failed_authentication");

        Exchange ex;
        try {
            ex = exchangeService.getExchangeByAcceptCode(acceptCode);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        // if the user that is accepting/rejecting the exchange is the one that should
        if (ex.getOfferer().getBook().getOwner().getUserId() == loggedUserAdvice.getLoggedUser().getUserId()) {
            String exchangeView;
            try {
                exchangeView = exchangeService.exchange(acceptCode, state);
            } catch (BadRequestException e) {
                LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
                return new ModelAndView("redirect:/400");
            } catch (NotFoundException e) {
                LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
                return new ModelAndView("redirect:/404");
            }
            mav = new ModelAndView(exchangeView);
        }
        return mav;
    }


    @RequestMapping("/confirm_offerer")
    public ModelAndView confirmExchangeOffer(@RequestParam(name = "accept_code") int accept_code) {
        Exchange exchange;
        try {
            exchange = exchangeService.getExchangeByAcceptCode(accept_code);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        // if the user that is accepting/rejecting the exchange is the one that should
        if (exchange.getOfferer().getBook().getOwner().getUserId() == loggedUserAdvice.getLoggedUser().getUserId()) {
            try {
                exchangeService.cofirmOfferer(accept_code);
            } catch (BadRequestException e) {
                LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
                return new ModelAndView("redirect:/400");
            } catch (NotFoundException e) {
                LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
                return new ModelAndView("redirect:/404");
            }
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
        Exchange exchange;
        try {
            exchange = exchangeService.getExchangeByAcceptCode(accept_code);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        // if the user that is accepting/rejecting the exchange is the one that should
        if (exchange.getRequester().getBook().getOwner().getUserId() == loggedUserAdvice.getLoggedUser().getUserId()) {
            try {
                exchangeService.cofirmRequester(accept_code);
            } catch (BadRequestException e) {
                LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
                return new ModelAndView("redirect:/400");
            } catch (NotFoundException e) {
                LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
                return new ModelAndView("redirect:/404");
            }
        }
        return new ModelAndView("redirect:/requests");
    }

    @GetMapping("/start_exchange")
    public ModelAndView startExchange(@ModelAttribute("exchangeForm") ExchangeForm exchangeForm, BindingResult errors, @RequestParam(name = "publication_id") long publicationId) {
        final ModelAndView mav = new ModelAndView("/exchange/solicit_exchange");
        Publication publication;
        try {
            publication = publicationService.getPublicationByPublicationId(publicationId);
        } catch (ApplicationRuntimeException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        List<Book> availableBooks;

        // NOTE: en el caso de que se haga una paginación de esta sección
        //  no hace falta realizar una excepción sino HAY QUE HACER UNA EXCEPCIÓN
        availableBooks = bookService.getAvailableBooksByUser(loggedUserAdvice.getLoggedUser());

        mav.addObject("availableBooks", availableBooks);
        mav.addObject("exchangeForm", exchangeForm);
        mav.addObject("publication", publication);
        return mav;
    }

    @PostMapping(path = "/exchange/initializeexchange")
    public ModelAndView initializeExchange(@NotEmpty @Valid @ModelAttribute("exchangeForm") ExchangeForm exchangeInput, BindingResult errors) {
        if (errors.hasErrors()) {
            return startExchange(exchangeInput, errors, exchangeInput.getPublicationId());
        }

        try {
            exchangeService.initializeExchange(exchangeInput.getBookId(), exchangeInput.getLocation(), exchangeInput.getPublicationId());
        } catch (BadRequestException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        } catch (NotFoundException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        return new ModelAndView("redirect:/requests");
    }

    @RequestMapping(path = "/submitReview", method = RequestMethod.POST)
    public ModelAndView submitReview(
            @RequestParam("exchangeId") long exchangeId,
            @RequestParam("reviewDescription") String reviewDescription,
            @RequestParam("userReviewRating") int userReviewRating/*,
		BindingResult result, RedirectAttributes redirectAttributes*/) {

        boolean success;
        try {
            success = userReviewService.createUserReview(exchangeId, loggedUserAdvice.getLoggedUser().getUserId(), reviewDescription, userReviewRating);
        } catch (BadRequestException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/400");
        } catch (NotFoundException e) {
            LOGGER.error(e.getExceptionMessage(), e.getStatusCode());
            return new ModelAndView("redirect:/404");
        }

        /*
        if (success) {
            return new ModelAndView("redirect:/successPage");
        } else {
            return new ModelAndView("redirect:/errorPage");
        }*/

        return new ModelAndView("redirect:/requests");
    }

}
