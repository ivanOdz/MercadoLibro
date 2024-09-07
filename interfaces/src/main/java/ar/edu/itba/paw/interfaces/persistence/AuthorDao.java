package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Author;

import java.util.List;
import java.util.Locale;

public interface AuthorDao {

    Author createAuthor(String authorName);

    List<Author> getAuthorsById(List<Long> authorId);

}
