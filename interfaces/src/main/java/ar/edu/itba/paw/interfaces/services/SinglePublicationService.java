package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SinglePublicationService {

    Publication createPublication(long userId,String isbn, String title, List<String> authors, String editorial, String description, Genres genre, BookState bookState, PublicationState publicationState, int edition, int rating, long image, String location);

}
