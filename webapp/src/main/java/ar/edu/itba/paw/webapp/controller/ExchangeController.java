package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;

import ar.edu.itba.paw.webapp.dto.input.MessageDTO;
import ar.edu.itba.paw.webapp.dto.input.UpdateExchangeDTO;
import ar.edu.itba.paw.webapp.dto.output.ExchangeDTO;
import ar.edu.itba.paw.webapp.dto.output.MessagesDTO;
import ar.edu.itba.paw.webapp.mediaTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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


    // CHECK: application message same output and input
    @POST
    @Path("/{id}/messages")
    @Consumes(value = {VndType.APPLICATION_MESSAGE})
    public Response sendMessage(@PathParam("id") long exchangeId, MessageDTO messageDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        exchangeService.createMessage(exchangeId, user, messageDTO.getMessage());
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/messages")
    @Produces(value = {VndType.APPLICATION_MESSAGE})
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

    // IMPLEMENT: GET /exchanges/{id}

    @PATCH
    @Path("/{id}")
    @Consumes(value = {VndType.APPLICATION_UPDATE_EXCHANGE})
    public Response updateExchange(@PathParam("id") final Long exchangeId,
                                   @QueryParam("accepted") final Boolean accepted,
                                   @QueryParam("confirm") final Boolean requester,
                                   UpdateExchangeDTO updateExchangeDTO) {
        exchangeService.updateExchange(updateExchangeDTO.getAcceptCode(), accepted, requester);
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
