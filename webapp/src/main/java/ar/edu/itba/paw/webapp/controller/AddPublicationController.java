package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.webapp.form.PublicationForm;
import ar.edu.itba.paw.interfaces.services.SinglePublicationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

import javax.validation.Valid;

@Controller
public class AddPublicationController {

    private SinglePublicationService ps;

    private ImageService imageService;

    public AddPublicationController(final SinglePublicationService ps, final ImageService imageService) {
        this.ps = ps;
        this.imageService = imageService;
    }

    @GetMapping(path = "/createPublication")
    public ModelAndView createPublicationForm(@ModelAttribute("publicationForm") PublicationForm publicationForm) {
		
    	Locale locale = new Locale("es");
        final ModelAndView mav = new ModelAndView("/add/createPublication");
        
        mav.addObject("bookStates", BookState.getNames(locale));
        mav.addObject("genres", Genres.getNames(locale));
        
        return mav;
    }
    
    @PostMapping(path = "/createPublication")
    public ModelAndView addPublication(@Valid @ModelAttribute("publicationForm") PublicationForm publicationForm,
                                       BindingResult errors,
                                       @RequestParam("imageFile") MultipartFile imageFile) {

        final Image image = imageService.saveImage(imageFile);


        if (errors.hasErrors()) {
            return createPublicationForm(publicationForm);
        }




        final Publication publication = ps.createPublication(	publicationForm.getUsername(),
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
                                                                image.getImageId(),
        														publicationForm.getLocation()
        													);

        
        
        return new ModelAndView("redirect:/");
    }
}
