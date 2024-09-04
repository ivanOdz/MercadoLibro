package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.BookStateWrapper;
import ar.edu.itba.paw.models.utils.GenreWrapper;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.webapp.form.PublicationForm;
import ar.edu.itba.paw.interfaces.services.BookStateService;
import ar.edu.itba.paw.interfaces.services.GenreService;
import ar.edu.itba.paw.interfaces.services.SinglePublicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@Controller
public class AddPublicationController {

    private SinglePublicationService ps;

    private ImageService imageService;

    @Autowired
    private GenreService genreService;
    @Autowired
    private BookStateService bookStateService;


    public AddPublicationController(final SinglePublicationService ps, final ImageService imageService) {
        this.ps = ps;
        this.imageService = imageService;
    }

    @GetMapping(path = "/createPublication")
    public ModelAndView createPublicationForm(@ModelAttribute("publicationForm") PublicationForm publicationForm) {
		
		final ModelAndView mav = new ModelAndView("/add/createPublication");
			
		if (publicationForm.getAuthors() == null) {
			
			publicationForm.setAuthors(new ArrayList<String>());
		}
		
		mav.addObject("publicationForm", publicationForm);
		mav.addObject("genres", List.of(Genres.values()).stream().map(genre -> new GenreWrapper(genre, genreService.getGenreDisplayName(genre))).collect(Collectors.toList()));
		mav.addObject("bookStates", List.of(BookState.values()).stream().map(bookStatus -> new BookStateWrapper(bookStatus, bookStateService.getBookStateDisplayName(bookStatus))).collect(Collectors.toList()));
		
		return mav;
    }

    @PostMapping(path = "/createPublication")
    public ModelAndView addPublication(@Valid @ModelAttribute("publicationForm") PublicationForm publicationForm,
                                       BindingResult errors,
                                       MultipartFile imageFile) {

        List<String> authors = publicationForm.getAuthors(); // [!] Debug
        Genres genre = publicationForm.getGenre();			 // [!] Debug
        BookState state = publicationForm.getBookState();	 // [!] Debug
        System.out.println("Autores: " + authors); 			 // [!] Debug
        System.out.println("Genero: " + genre);				 // [!] Debug
        System.out.println("Estado: " + state);				 // [!] Debug

		if (errors.hasErrors()) {
			return createPublicationForm(publicationForm);
		}
		
        final Image image = imageService.saveImage(imageFile);

        final Publication publication = ps.createPublication(publicationForm.getUsername(),
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
