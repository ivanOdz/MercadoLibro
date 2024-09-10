package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import ar.edu.itba.paw.webapp.form.BookForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@Controller
public class AddPublicationController {

    private SinglePublicationService ps;

    private ImageService imageService;

    private EmailService emailService;

    private ExchangeService exchangeService;

    private PublicationService publicationService;

    private BookService bookService;

    private UserService userService;

    @Autowired
    private GenreService genreService;
    @Autowired
    private BookStateService bookStateService;


    public AddPublicationController(final SinglePublicationService ps, final ImageService imageService, final EmailService emailService, final ExchangeService exchangeService, PublicationService publicationService, BookService bookService, UserService userService) {
        this.ps = ps;
        this.imageService = imageService;
        this.emailService = emailService;
        this.exchangeService = exchangeService;
        this.publicationService = publicationService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping(path = "/createpublication")
    public ModelAndView createPublicationForm(@ModelAttribute("publicationForm") BookForm bookForm, @RequestParam(name = "publication_id") long publicationId, @RequestParam(name = "is_for_exchange") boolean isForExchange, @RequestParam(name = "submited_mail", defaultValue = "") String submited_mail) {

        final ModelAndView mav = new ModelAndView("/add/createPublication");

        if (bookForm.getAuthors() == null) {
            bookForm.setAuthors(new ArrayList<>());
        }

        String username = userService.findUsernameByEmail(submited_mail);
        mav.addObject("publicationForm", bookForm);
        mav.addObject("genres", List.of(Genre.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
        mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));
        mav.addObject("publicationId", publicationId);
        mav.addObject("isForExchange", isForExchange);
        mav.addObject("submited_mail", submited_mail);
        mav.addObject("username", username);

        return mav;
    }

    @PostMapping(path = "/createpublication")
    public ModelAndView addPublication(@Valid @ModelAttribute("publicationForm") BookForm bookForm,
                                       BindingResult errors,
                                       @RequestParam(name = "publication_id") long publicationId, @RequestParam(name = "is_for_exchange") boolean isForExchange, @RequestParam(name = "submited_mail", defaultValue = "") String submited_mail) {

		if (errors.hasErrors()) {
            System.out.println(errors.getAllErrors());
			return createPublicationForm(bookForm, publicationId, isForExchange, submited_mail);
		}


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user;

        Publication publication;
        if (authentication.getPrincipal() instanceof PawUserDetails pud){
            user = pud.getUser();
           /* publication = ps.createPublication(
                    user.getUserId(),
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
                Publication offererPub = publicationService.getPublicationById(ex.getOffererPubId()).get();
                Publication requesterPub = publicationService.getPublicationById(ex.getRequesterPubId()).get();

                Book bookOffered = bookService.getBookById(offererPub.getBookId()).get();
                Book bookRequested = bookService.getBookById(requesterPub.getBookId()).get();

                User oferrer = userService.findById(offererPub.getUserId()).get();
                User requester = userService.findById(requesterPub.getUserId()).get();

                String oferrerEmail = oferrer.getMail();

                variables.put("requesterEmail", requester.getMail());
                variables.put("requesterName", requester.getUsername());
                //variables.put("requestedPublication", bookRequested.getTitle());
                //variables.put("offeredPublication", bookOffered.getTitle());
                variables.put("validationUrl", "http://localhost:8080/exchange?accept_code=" + ex.getAcceptCode() + "&state=true");
                variables.put("rejectionUrl", "http://localhost:8080/exchange?accept_code=" + ex.getAcceptCode() +"&state=false");

                emailService.sendEmail(oferrerEmail, variables, "exchangeRequest", "Requesting");
            }*/
            return new ModelAndView("redirect:/");
        } else {
            return new ModelAndView("redirect:/login");
        }
    }
}
