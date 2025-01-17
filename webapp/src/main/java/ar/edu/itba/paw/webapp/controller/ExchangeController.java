package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import ar.edu.itba.paw.webapp.dto.input.ConfirmExchangeDTO;
import ar.edu.itba.paw.webapp.dto.input.MessageDTO;
import ar.edu.itba.paw.webapp.form.MessageForm;
import ar.edu.itba.paw.webapp.form.UserReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("exchanges")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserReviewService userReviewService;

    @Context
    private UriInfo uriInfo;

    /*
    @PostMapping(path = "/exchange/initializeexchange")
    public ModelAndView initializeExchange(@NotEmpty @Valid @ModelAttribute("exchangeForm") ExchangeForm exchangeInput, BindingResult errors, @ModelAttribute("loggedUser") User loggeduser) {
        if (errors.hasErrors()) {
            return startExchange(exchangeInput, errors, exchangeInput.getPublicationId(), loggeduser);
        }

        return new ModelAndView("redirect:/requests");
    }*/

    @POST
    public Response createExchange(@QueryParam("book") final Integer bookId, @QueryParam("publication") final Integer pubId, @QueryParam("location") final Integer locationId) {
        Exchange exchange = exchangeService.initializeExchange(bookId, locationId, pubId);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(exchange.getExchangeId())).build()).build();
    }

    /*
    @PostMapping( "/send_message")
    public ResponseEntity<Void> sendMessage(@RequestParam("chatExchangeId") long exchangeId,
                            @RequestParam("chatUserId") long userId,
                            @RequestParam("message") String message) {
            exchangeService.createMessage(exchangeId, userId, message);
        return ResponseEntity.ok().build();
    }
    */

    @PATCH
    @Path("/{id}/message")
    public Response sendMessage(@PathParam("id") long exchangeId, MessageDTO messageDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        exchangeService.createMessage(exchangeId, user, messageDTO.getMessage());
        return Response.noContent().build();
    }

    /*
    @RequestMapping("/createexchange")
    public ModelAndView exchange(@RequestParam(name = "accept_code") int acceptCode, @RequestParam(name = "state") boolean state, @ModelAttribute("loggedUser") User loggeduser) {
        if(exchangeService.exchange(acceptCode, state)){
            return new ModelAndView("exchange/accepted");
        }
        return new ModelAndView("exchange/rejected");
    }*/

    @PATCH
    @Path("/{id}/start")
    public Response startExchange(@PathParam("id") Integer exchangeId) {
        exchangeService.exchange(exchangeId, true);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/reject")
    public Response rejectExchange(@PathParam("id") Integer exchangeId) {
        exchangeService.exchange(exchangeId, false);
        return Response.noContent().build();
    }


    // CHECK: exchangeId not used and could be a better way to obtain the logged user
    @PATCH
    @Path("/{id}/confirm_offer")
    public Response confirmExchangeOffer(@PathParam("id") Integer exchangeId, ConfirmExchangeDTO confirmExchangeDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        exchangeService.confirmOffer(user.getUserId(), confirmExchangeDTO.getAcceptCode());
        return Response.noContent().build();
    }

    // CHECK: exchangeId not used and could be a better way to obtain the logged user
    @PATCH
    @Path("/{id}/confirm_request")
    public Response confirmExchangeRequest(@PathParam("id") Integer exchangeId, ConfirmExchangeDTO confirmExchangeDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        exchangeService.confirmRequest(user.getUserId(), confirmExchangeDTO.getAcceptCode());
        return Response.noContent().build();
    }

    //Screens

    /*
    @GetMapping("/start_exchange")
    public ModelAndView startExchange(@ModelAttribute("exchangeForm") ExchangeForm exchangeForm, BindingResult errors, @RequestParam(name = "publication_id") long publicationId, @ModelAttribute("loggedUser") User loggeduser) {
        final ModelAndView mav = new ModelAndView("/exchange/solicit_exchange");
        Publication publication;

        publication = publicationService.getPublicationByPublicationId(publicationId);
        List<Book> availableBooks = bookService.getAvailableBooksByUser(loggeduser);

        mav.addObject("availableBooks", availableBooks);
        mav.addObject("exchangeForm", exchangeForm);
        mav.addObject("publication", publication);

        return mav;
    }*/

    /*
    @RequestMapping("/exchange/accepted")
    public ModelAndView exchangeAccepted() {
        return new ModelAndView("exchange/accepted");
    }

    @RequestMapping("/exchange/invalid")
    public ModelAndView exchangeRejected() {
        return new ModelAndView("/exchange/invalid");
    }*/

}
