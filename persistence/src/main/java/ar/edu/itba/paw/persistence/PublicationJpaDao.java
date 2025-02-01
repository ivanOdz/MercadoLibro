package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static ar.edu.itba.paw.models.utils.Constants.*;

@Primary
@Repository
public class PublicationJpaDao implements PublicationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Publication createPublication(Book book, User user, List<Location> locations, PublicationState publicationState) {
        final Publication publication = new Publication(null, book, user,publicationState, new Timestamp(new Date().getTime()), locations);
        book.setAvailable(false);
        em.persist(publication);
        return publication;
    }

    @Override
    public void terminatePublication(Publication publication) {
        publication.setPublicationState(PublicationState.TERMINATED);
        em.merge(publication);
    }

    @Override
    public Optional<Publication> getPublicationByPublicationId(long publicationId) {
        return Optional.ofNullable(em.find(Publication.class, publicationId));
    }

    @Override
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, BookState state, Genre genre, String sortType, int page, User currentUser, Long locationId) {
        Long userId = (currentUser != null ? currentUser.getUserId() : null);

        if (page < 0) {
            page = 0;
        }

        SortType sort = SortType.fromString(sortType);
        sort = sort == null ? DEFAULT_PUBLICATION_SORT_TYPE : sort;

        StringBuilder nativeQueryString = new StringBuilder(
                "SELECT DISTINCT p.publicationid " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                        "LEFT JOIN publication_location pl ON p.publicationId = pl.publicationId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' "
        );

        if(userId != null){
            nativeQueryString.append("AND p.userId = :userId ");
        }

        if (genre != null) {
            nativeQueryString.append("AND bm.genre = :genre ");
        }

        if (state != null) {
            nativeQueryString.append("AND b.bookState = :state ");
        }

        if(locationId != null){
            nativeQueryString.append("AND pl.locationId = :locationId ");
        }

        switch (sort) {
            case RATING_ASCENDING:
                nativeQueryString.append(" GROUP BY p.publicationid ");
                nativeQueryString.append(" ORDER BY COALESCE(AVG(br.rating), 0) ASC");
                break;
            case RATING_DESCENDING:
                nativeQueryString.append(" GROUP BY p.publicationid ");
                nativeQueryString.append(" ORDER BY COALESCE(AVG(br.rating), 0) DESC");
                break;
            case BOOK_NAME_ASCENDING:
                nativeQueryString.append(" GROUP BY p.publicationid, bm.title ");
                nativeQueryString.append(" ORDER BY bm.title ASC");
                break;
            case BOOK_NAME_DESCENDING:
                nativeQueryString.append(" GROUP BY p.publicationid, bm.title ");
                nativeQueryString.append(" ORDER BY bm.title DESC");
                break;
            case PUBLICATION_DATE_DESCENDING:
                nativeQueryString.append(" GROUP BY p.publicationid ");
                nativeQueryString.append(" ORDER BY p.publicationdatetime DESC");
                break;
            default:
                nativeQueryString.append(" GROUP BY p.publicationid ");
                nativeQueryString.append(" ORDER BY p.publicationdatetime ASC");
        }

        Query nativeQuery = em.createNativeQuery(nativeQueryString.toString());

        nativeQuery.setParameter("publicationState", PublicationState.CURRENT.toString());

        if (userId != null) {
            nativeQuery.setParameter("userId", userId);
        }
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        nativeQuery.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(genre != null){
            nativeQuery.setParameter("genre", genre.toString());
        }

        if(state != null){
            nativeQuery.setParameter("state", state.toString());
        }

        if(locationId != null){
            nativeQuery.setParameter("locationId", locationId);
        }

        nativeQuery.setMaxResults(PUBLICATIONS_PAGE_SIZE);
        nativeQuery.setFirstResult(page * PUBLICATIONS_PAGE_SIZE);

        @SuppressWarnings("unchecked")
        List<Long> publicationIds = nativeQuery.getResultList().stream().mapToLong(n -> ((Number) n).longValue()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);


        String jpqlQuery = "FROM Publication p WHERE p.publicationId IN (:ids) ";
        switch (sort) {
            case RATING_ASCENDING:
                jpqlQuery += "ORDER BY p.book.bookModel.averageRating ASC";
                break;
            case RATING_DESCENDING:
                jpqlQuery += "ORDER BY p.book.bookModel.averageRating DESC";
                break;
            case BOOK_NAME_ASCENDING:
                jpqlQuery += "ORDER BY p.book.bookModel.title ASC";
                break;
            case BOOK_NAME_DESCENDING:
                jpqlQuery += "ORDER BY p.book.bookModel.title DESC";
                break;
            case PUBLICATION_DATE_DESCENDING:
                jpqlQuery += "ORDER BY p.publicationDatetime DESC";
                break;
            default:
                jpqlQuery += "ORDER BY p.publicationDatetime ASC";
        }

        TypedQuery<Publication> query = em.createQuery(jpqlQuery, Publication.class);
        query.setParameter("ids",publicationIds);

        int totalResults = getTotalResultsByBook(userId, safeSearch, genre, state, locationId);

        return new PaginatedResponse<>(query.getResultList(), new ItemFilterMetadata(page, PUBLICATIONS_PAGE_SIZE, totalResults, search, genre, sort, null,  state, null));
    }

    @Override
    public PaginatedResponse<Publication, ItemFilterMetadata> getFavoritePublications(String search, BookState state, Genre genre, int page, User currentUser, Long locationId) {
        Long userId = currentUser.getUserId();

        if (page < 0) {
            page = 0;
        }

        StringBuilder nativeQueryString = new StringBuilder("SELECT fp.publicationid " +
                "FROM favorite_publication fp " +
                "JOIN publication p ON p.publicationid = fp.publicationid " +
                "JOIN book b ON p.bookId = b.bookId " +
                "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                "LEFT JOIN publication_location pl ON p.publicationId = pl.publicationId " +
                "WHERE fp.userid = :userId AND p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' " +
                "AND p.userId = :userId "
                );

//"ORDER BY "

        if (genre != null) {
            nativeQueryString.append("AND bm.genre = :genre ");
        }

        if (state != null) {
            nativeQueryString.append("AND b.bookState = :state ");
        }

        if(locationId != null){
            nativeQueryString.append("AND pl.locationId = :locationId ");
        }

        nativeQueryString.append(" GROUP BY fp.publicationId ORDER BY liked_at DESC ");

        Query nativeQuery = em.createNativeQuery(nativeQueryString.toString());

        nativeQuery.setParameter("publicationState", PublicationState.CURRENT.toString());
        nativeQuery.setParameter("userId", userId);

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        nativeQuery.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(genre != null){
            nativeQuery.setParameter("genre", genre.toString());
        }

        if(state != null){
            nativeQuery.setParameter("state", state.toString());
        }

        if(locationId != null){
            nativeQuery.setParameter("locationId", locationId);
        }

        nativeQuery.setMaxResults(PUBLICATIONS_PAGE_SIZE);
        nativeQuery.setFirstResult(page * PUBLICATIONS_PAGE_SIZE);

        @SuppressWarnings("unchecked")
        List<Long> favoritePublicationIds = nativeQuery.getResultList().stream().mapToLong(n -> ((Number) n).longValue()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);


        if (favoritePublicationIds.isEmpty()) {
            return new PaginatedResponse<>(Collections.emptyList(), new ItemFilterMetadata(page, PUBLICATIONS_PAGE_SIZE, 0, search, genre, null, null, state, null));
        }

        String jpqlQuery = """
                SELECT fp.publication
                FROM FavoritePublication fp
                WHERE fp.publication.publicationId IN (:ids)
                AND fp.user.userId = :userId
                AND fp.publication.publicationState = 'CURRENT'
                ORDER BY fp.likedAt DESC
            """;

        TypedQuery<Publication> query = em.createQuery(jpqlQuery, Publication.class);
        query.setParameter("ids",favoritePublicationIds);
        query.setParameter("userId", userId);

        List<Publication> favoritePublications = query.getResultList();

        int totalResults = getTotalResultsFavoritePublications(userId, safeSearch, genre, state, locationId);

        return new PaginatedResponse<>(favoritePublications, new ItemFilterMetadata(page, PUBLICATIONS_PAGE_SIZE, totalResults, search, genre, null, null, state, null));
    }

    @Override
    public int getPublicationCountByUserId(long userId) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM publication p WHERE p.userId = :userId");

        Query nativeQuery = em.createNativeQuery(query.toString());
        nativeQuery.setParameter("userId", userId);

        return ((Number) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public List<GenreWrapper> getGenreQtyByPublication(Long userId, String search, BookState state) {
        StringBuilder sqlQuery =  new StringBuilder("SELECT bm.genre, COUNT(*) AS genreCount " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' ");

        if(userId != null){
            sqlQuery.append("AND p.userId = :userId ");
        }

        if(state != null){
            sqlQuery.append("AND b.bookState = :state ");
        }

        sqlQuery.append("GROUP BY bm.genre");

        Query query = em.createNativeQuery(sqlQuery.toString());

        query.setParameter("publicationState", PublicationState.CURRENT.toString());
        if (userId != null) {
            query.setParameter("userId", userId);
        }

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(state != null){
            query.setParameter("state", state.toString());
        }

        List<Object[]> results = query.getResultList();

        List<GenreWrapper> genreWrappers = new ArrayList<>();
        for (Object[] result : results) {
            String genreValue = result[0].toString();  // bm.genre (STRING)
            Genre genre = Genre.valueOf(genreValue);
            int genreCount = ((Number) result[1]).intValue();  // genreCount
            genreWrappers.add(new GenreWrapper(genre, genreCount));
        }

        return genreWrappers;
    }


    @Override
    public List<BookStateWrapper> getBookStateQtyByPublication(Long userId, String search, Genre genre) {
        StringBuilder sqlQuery =  new StringBuilder("SELECT b.bookState, COUNT(*) AS stateCount " +
                "FROM publication p " +
                "JOIN book b ON p.bookId = b.bookId " +
                "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' ");

        if(userId != null){
            sqlQuery.append("AND p.userId = :userId ");
        }

        if(genre != null){
            sqlQuery.append("AND bm.genre = :genre ");
        }

        sqlQuery.append("GROUP BY b.bookState");

        Query query = em.createNativeQuery(sqlQuery.toString());

        if (userId != null) {
            query.setParameter("userId", userId);
        }

        query.setParameter("publicationState", PublicationState.CURRENT.toString());
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(genre != null){
            query.setParameter("genre", genre.toString());
        }

        List<Object[]> results = query.getResultList();

        List<BookStateWrapper> bookStateWrappers = new ArrayList<>();
        for (Object[] result : results) {
            String bookStateValue = result[0].toString();
            BookState bookState = BookState.valueOf(bookStateValue);
            int bookStateCount = ((Number) result[1]).intValue();
            bookStateWrappers.add(new BookStateWrapper(bookState, bookStateCount));
        }

        return bookStateWrappers;
    }


    private int getTotalResultsByBook(Long userId, String search, Genre genre, BookState state, Long locationId) {
        StringBuilder nativeQueryString = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "LEFT JOIN publication_location pl ON pl.publicationId = p.publicationId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' "
        );

        if(userId != null){
            nativeQueryString.append("AND p.userId = :userId ");
        }

        if (genre != null) {
            nativeQueryString.append("AND bm.genre = :genre ");
        }

        if (state != null) {
            nativeQueryString.append("AND b.bookState = :state ");
        }

        if (locationId != null) {
            nativeQueryString.append("AND pl.locationId = :locationId ");
        }

        Query query = em.createNativeQuery(nativeQueryString.toString());

        query.setParameter("publicationState", PublicationState.CURRENT.toString());

        if (userId != null) {
            query.setParameter("userId", userId);
        }

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if (genre != null) {
            query.setParameter("genre", genre.toString());
        }

        if (state != null) {
            query.setParameter("state", state.toString());
        }

        if (locationId != null) {
            query.setParameter("locationId", locationId);
        }


        return ((Number) query.getSingleResult()).intValue();
    }

    private int getTotalResultsFavoritePublications(Long userId, String search, Genre genre, BookState state, Long locationId) {
        StringBuilder nativeQueryString = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM favorite_publication fp " +
                        "JOIN publication p ON p.publicationId = fp.publicationId " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "LEFT JOIN publication_location pl ON pl.publicationId = p.publicationId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' " );

        nativeQueryString.append("AND p.userId = :userId ");

        if (genre != null) {
            nativeQueryString.append("AND bm.genre = :genre ");
        }

        if (state != null) {
            nativeQueryString.append("AND b.bookState = :state ");
        }

        if (locationId != null) {
            nativeQueryString.append("AND pl.locationId = :locationId ");
        }

        Query query = em.createNativeQuery(nativeQueryString.toString());

        query.setParameter("publicationState", PublicationState.CURRENT.toString());

        query.setParameter("userId", userId);
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if (genre != null) {
            query.setParameter("genre", genre.toString());
        }

        if (state != null) {
            query.setParameter("state", state.toString());
        }

        if (locationId != null) {
            query.setParameter("locationId", locationId);
        }

        return ((Number) query.getSingleResult()).intValue();
    }


    @Override
    public void deletePublication(long publicationId) {
        Publication publication = em.find(Publication.class, publicationId);
        em.remove(publication);
    }

    @Override
    public void addLocation(Publication publication, Location location) {
        publication.addLocation(location);
        em.merge(publication);
    }

    @Override
    public List<Publication> getActivePublicationsByUser(User user) {
        TypedQuery<Publication> query = em.createQuery("FROM Publication p WHERE p.user = :user AND p.publicationState = :publicationState OR p.publicationState =:state", Publication.class);
        query.setParameter("publicationState", PublicationState.CURRENT);
        query.setParameter("state", PublicationState.OFFERED);
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public Optional<FavoritePublication> getFavoritePublicationById(long fpId) {
        return Optional.ofNullable(em.find(FavoritePublication.class, fpId));
    }

    @Override
    public Optional<FavoritePublication> getFavoritePublicationFromUser(long publicationId, long userId) {
        TypedQuery<FavoritePublication> query = em.createQuery("FROM FavoritePublication fp WHERE fp.user.id = :userId AND fp.publication.id = :publicationId", FavoritePublication.class);
        query.setParameter("publicationId", publicationId);
        query.setParameter("userId", userId);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public FavoritePublication markFavoritePublication(long publicationId, long userId) {
        User user = em.find(User.class, userId);
        Publication publication = em.find(Publication.class, publicationId);
        FavoritePublication newFavorite = new FavoritePublication(publication, user, Timestamp.valueOf(LocalDateTime.now()));
        em.persist(newFavorite);
        return newFavorite;
    }

    @Override
    public void unmarkFavoritePublication(long favPubId) {
        FavoritePublication favoritePublication = em.find(FavoritePublication.class, favPubId);
        em.remove(favoritePublication);
    }
}