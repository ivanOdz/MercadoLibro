package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;

import java.util.List;

public interface BookDao {

    Book createBook(String isbn, String title, List<String> author, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, long userId);

}
