package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    Book createBook(String isbn, String title, List<String> author, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, long userId);

}
