package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.BookNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.PublicationBadRequestException;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Repository
public class PublicationJpaDao implements PublicationDao {

    @Autowired
    MessageSource messageSource;

    @PersistenceContext
    private EntityManager em;

    // TODO: Manejo de excepciones.
    public Long createPublication(long bookId, PublicationState publicationState, Timestamp publicationDatetime, Location location) {
        try {
            // ASK: esta bien hacer esto directamente en vez de llamar al getBookById del BookJpaDao?
            Book book = em.find(Book.class, bookId);
            final Publication publication = new Publication(null, book, publicationState, publicationDatetime, location);
            em.persist(publication);
            return publication.getPublicationId();
        } catch (BookNotFoundException e) {
            throw new IllegalArgumentException(messageSource.getMessage("book.not.found", null, null));
        } catch (PublicationBadRequestException e) {
            throw new IllegalArgumentException(messageSource.getMessage("publication.creation.error", null, null));
        }
    }

    @Override
    public Long createPublication(long bookId, long userId, Set<Location> location, PublicationState publicationState) {
        // ASK: esta bien hacer esto directamente en vez de llamar al getBookById del BookJpaDao?
        Book book = em.find(Book.class, bookId);
        final Publication publication = new Publication(null, book, publicationState, new Timestamp(new Date().getTime()), location);
        em.persist(publication);
        return publication.getPublicationId();
    }


    @Override
    public void terminatePublication(long pubId) {
        Publication publication = em.find(Publication.class, pubId);
        publication.setPublicationState(PublicationState.TERMINATED);
        em.merge(publication);
    }

    @Override
    public Publication getPublicationByPublicationId(long publicationId) {
        return null;
    }

    @Override
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage) {
        return null;
    }

    @Override
    public int getPublicationCountByUserId(long userId) {
        return 0;
    }

    @Override
    public List<BookStateWrapper> getBookStateQtyByPublication(String search, boolean isGenreFilterActive, Genre genreFilter) {
        return List.of();
    }

    @Override
    public List<GenreWrapper> getGenreQtyByPublication(String search, boolean isBookStateFilterActive, BookState bookStateFilter) {
        return List.of();
    }
}