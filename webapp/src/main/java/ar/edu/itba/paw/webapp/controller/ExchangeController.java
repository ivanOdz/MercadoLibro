package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import ar.edu.itba.paw.webapp.dto.input.ConfirmExchangeDTO;
import ar.edu.itba.paw.webapp.dto.input.MessageDTO;
import ar.edu.itba.paw.webapp.dto.output.ExchangeDTO;
import ar.edu.itba.paw.webapp.dto.output.MessagesDTO;
import ar.edu.itba.paw.webapp.form.MessageForm;
import ar.edu.itba.paw.webapp.form.UserReviewForm;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
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

    @GET
    @Produces(value = {VndType.APPLICATION_EXCHANGE})
    public Response getExchanges(@QueryParam("id") final long userId,
                                 @QueryParam("state") final ExchangeState state,
                                 @QueryParam("isOfferer") @DefaultValue("false") final Boolean isOfferer,
                                 @QueryParam("isRequester") @DefaultValue("false") final Boolean isRequester,
                                 @QueryParam("page") final Integer page) {
        PaginatedResponse<Exchange, BasicMetadata> exchanges = exchangeService.getExchanges(userId, state, isOfferer, isRequester, page);

        List<ExchangeDTO> exchangeDTOS = exchanges.getData().stream().map(exchange -> ExchangeDTO.fromExchange(uriInfo, exchange)).toList();

        return Response.ok(new GenericEntity<List<ExchangeDTO>>(exchangeDTOS) {}).build();
    }


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
    @Consumes(value = {VndType.APPLICATION_MESSAGE})
    public Response sendMessage(@PathParam("id") long exchangeId, MessageDTO messageDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        exchangeService.createMessage(exchangeId, user, messageDTO.getMessage());
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/messages")
    public Response getMessages(@PathParam("id") long exchangeId) {
        List<Message> m = exchangeService.getMessages(exchangeId);
        List<MessagesDTO> messages = m.stream().map(message -> MessagesDTO.fromMessage(uriInfo, message)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<MessagesDTO>>(messages) {}).build();
    }

    @GET
    @Path("/{id}/messages/{message_id}")
    public Response getMessage(@PathParam("id") long exchangeId, @PathParam("message_id") long messageId) {
        Message m = exchangeService.getMessage(messageId);
        MessagesDTO message = MessagesDTO.fromMessage(uriInfo, m);
        return Response.ok(new GenericEntity<MessagesDTO>(message) {}).build();
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
