package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Author;

public interface AuthorDao {

    Author createAuthor(String authorName);

}
