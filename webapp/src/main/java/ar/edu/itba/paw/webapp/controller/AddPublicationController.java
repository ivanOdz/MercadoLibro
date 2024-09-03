package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.webapp.form.PublicationForm;
import ar.edu.itba.paw.interfaces.services.SinglePublicationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AddPublicationController {

    private SinglePublicationService ps;

    public AddPublicationController(final SinglePublicationService ps) {
        this.ps = ps;
    }

    @RequestMapping(path = "/singlePublication", method = RequestMethod.POST)
    public ModelAndView addPublication(@Valid @ModelAttribute("publicationForm") PublicationForm publicationForm, BindingResult errors) {
        if(errors.hasErrors()){
            return createPublicationForm(publicationForm);
        }
        final Publication publication = ps.createPublication(publicationForm.getUsername(), publicationForm.getMail(), publicationForm.getIsbn(), publicationForm.getTitle(), publicationForm.getAuthors(), publicationForm.getEditorial(), publicationForm.getDescription(), Genres.ADVENTURE, BookState.ACCEPTABLE, PublicationState.CURRENT, publicationForm.getEdition(), publicationForm.getRating(), publicationForm.getImage(), publicationForm.getLocation());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = "/singlePublication", method = RequestMethod.GET)
    public ModelAndView createPublicationForm(@ModelAttribute("publicationForm") PublicationForm publicationForm){
        return new ModelAndView("/add/singlePublication");
    }

}
