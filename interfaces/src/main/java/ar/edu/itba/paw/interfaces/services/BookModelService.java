package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.Genre;
import org.springframework.stereotype.Service;

@Service
public interface BookModelService {

    BookModel getBookModelByBookModelId(long bookModelId);


    void addBookModel(String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, int language, int dimension, Short publicationYear, boolean pocketEdition, boolean hardcover);
}
