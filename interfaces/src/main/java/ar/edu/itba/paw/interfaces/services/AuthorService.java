package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Author;
import org.springframework.stereotype.Service;

@Service
public interface AuthorService {

    Author createAuthor(String authorName);

}
