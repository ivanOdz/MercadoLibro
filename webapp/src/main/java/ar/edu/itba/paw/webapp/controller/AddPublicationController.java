package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.webapp.form.PublicationForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

@Controller
public class AddPublicationController {

    private SinglePublicationService ps;

    private ImageService imageService;

    private EmailService emailService;

    private ExchangeService exchangeService;

    private PublicationsService publicationsService;

    private BookService bookService;

    private UserService userService;

    @Autowired
    private GenreService genreService;
    @Autowired
    private BookStateService bookStateService;


    public AddPublicationController(final SinglePublicationService ps, final ImageService imageService, final EmailService emailService, final ExchangeService exchangeService, PublicationsService publicationsService, BookService bookService, UserService userService) {
        this.ps = ps;
        this.imageService = imageService;
        this.emailService = emailService;
        this.exchangeService = exchangeService;
        this.publicationsService = publicationsService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping(path = "/createPublication")
    public ModelAndView createPublicationForm(@ModelAttribute("publicationForm") PublicationForm publicationForm, @RequestParam(name = "publicationId") long publicationId,  @RequestParam(name = "isForExchange") boolean isForExchange) {
		
		final ModelAndView mav = new ModelAndView("/add/createPublication");
			
		if (publicationForm.getAuthors() == null) {
			
			publicationForm.setAuthors(new ArrayList<String>());
		}
		
		mav.addObject("publicationForm", publicationForm);
		mav.addObject("genres", List.of(Genres.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
		mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));
        mav.addObject("publicationId", publicationId);
        mav.addObject("isForExchange", isForExchange);

		return mav;
    }

    @PostMapping(path = "/createPublication")
    public ModelAndView addPublication(@Valid @ModelAttribute("publicationForm") PublicationForm publicationForm,
                                       BindingResult errors,
                                       @RequestParam(name = "publicationId") long publicationId, @RequestParam(name = "isForExchange") boolean isForExchange) {

		if (errors.hasErrors()) {
			return createPublicationForm(publicationForm, publicationId, isForExchange);
		}



        final Publication publication = ps.createPublication(
                publicationForm.getUsername(),
                publicationForm.getMail(),
                publicationForm.getIsbn(),
                publicationForm.getTitle(),
                publicationForm.getAuthors(),
                publicationForm.getEditorial(),
                publicationForm.getDescription(),
                publicationForm.getGenre(),
                publicationForm.getBookState(),
                PublicationState.CURRENT,
                publicationForm.getEdition(),
                publicationForm.getRating(),
                imageService.saveImage(publicationForm.getImageFile()).getImageId(),
                publicationForm.getLocation()
        );

         if(isForExchange) {
        	 
             Exchange ex = exchangeService.initializeExchange(isForExchange, publication.getPublicationId(), publicationId);
             
             Map<String, Object> variables = new HashMap<>();
             Publication offererPub = publicationsService.getPublicationById(ex.getOfferer()).get();
             Publication requesterPub = publicationsService.getPublicationById(ex.getRequester()).get();

             Book bookOffered = bookService.getBookById(offererPub.getBookId()).get();
             Book bookRequested = bookService.getBookById(requesterPub.getBookId()).get();

             User oferrer = userService.findById(offererPub.getUserId()).get();
             User requester = userService.findById(requesterPub.getUserId()).get();

             String oferrerEmail = oferrer.getMail();

             variables.put("requesterEmail", requester.getMail());
             variables.put("requesterName", requester.getUsername());
             variables.put("requestedPublication", bookRequested.getTitle());
             variables.put("offeredPublication", bookOffered.getTitle());
             variables.put("validationUrl", "http://localhost:8080/exchange?acceptCode=" + ex.getAcceptCode() + "&state=true");
             variables.put("rejectionUrl", "http://localhost:8080/exchange?acceptCode=" + ex.getAcceptCode() +"&state=false");

             emailService.sendEmail(oferrerEmail, variables, "exchangeRequest", "Requesting");
         }


        return new ModelAndView("redirect:/");
    }
}
