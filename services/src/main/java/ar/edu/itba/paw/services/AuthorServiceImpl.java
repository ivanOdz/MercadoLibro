package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AuthorDao;
import ar.edu.itba.paw.interfaces.services.AuthorService;
import ar.edu.itba.paw.models.Author;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public Author createAuthor(String authorName) {
        return authorDao.createAuthor(authorName);
    }
}
