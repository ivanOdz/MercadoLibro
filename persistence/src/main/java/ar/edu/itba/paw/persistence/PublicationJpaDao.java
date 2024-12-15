package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
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
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(Long userId, String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, String sortType, String currentPage, User currentUser) {
        int page;
        try {
            page = Integer.parseInt(currentPage);
            if (page < 0) {
                page = 0;
            }
        } catch (NumberFormatException e) {
            page = 0;
        }

        SortType sort = SortType.fromString(sortType);
        if(sort == null){
            sort = DEFAULT_PUBLICATION_SORT_TYPE;
        }


        StringBuilder nativeQueryString = new StringBuilder(
                "SELECT p.publicationid " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' "
        );

        if(userId!=null){
            nativeQueryString.append("AND p.userId = :userId ");
        }

        if (isGenreFilterActive) {
            nativeQueryString.append("AND bm.genre = :genre ");
        }

        if (isBookStateFilterActive) {
            nativeQueryString.append("AND b.bookState = :bookState ");
        }

//nativeQueryString.append(" GROUP BY p.publicationid ");

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

        if(isGenreFilterActive){
            nativeQuery.setParameter("genre", genreFilter.toString());
        }

        if(isBookStateFilterActive){
            nativeQuery.setParameter("bookState", bookStateFilter.toString());
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

        int totalResults = getTotalResultsByBook(userId, safeSearch, isGenreFilterActive, genreFilter, isBookStateFilterActive, bookStateFilter);

        PaginatedResponse<Publication, ItemFilterMetadata> paginatedResponse = new PaginatedResponse<>(query.getResultList(), new ItemFilterMetadata(page, PUBLICATIONS_PAGE_SIZE, totalResults, search, isGenreFilterActive, genreFilter, sort, null, isBookStateFilterActive, bookStateFilter, null));

        for(Publication publication : paginatedResponse.getData()){
            setIsLikedByUser(currentUser, publication);
        }

        return paginatedResponse;
    }

    @Override
    public int getPublicationCountByUserId(long userId) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM publication p WHERE p.userId = :userId");

        Query nativeQuery = em.createNativeQuery(query.toString());
        nativeQuery.setParameter("userId", userId);

        return ((Number) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public List<GenreWrapper> getGenreQtyByPublication(Long userId, String search, boolean isBookStateFilterActive, BookState bookStateFilter) {
        StringBuilder sqlQuery =  new StringBuilder("SELECT bm.genre, COUNT(*) AS genreCount " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' ");

        if(userId!=null){
            sqlQuery.append("AND p.userId = :userId ");
        }

        if(isBookStateFilterActive){
            sqlQuery.append("AND b.bookState = :bookState ");
        }

        sqlQuery.append("GROUP BY bm.genre");

        Query query = em.createNativeQuery(sqlQuery.toString());

        query.setParameter("publicationState", PublicationState.CURRENT.toString());
        if (userId != null) {
            query.setParameter("userId", userId);
        }

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(isBookStateFilterActive){
            query.setParameter("bookState", bookStateFilter.toString());
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
    public List<BookStateWrapper> getBookStateQtyByPublication(Long userId, String search, boolean isGenreFilterActive, Genre genreFilter) {
        StringBuilder sqlQuery =  new StringBuilder("SELECT b.bookState, COUNT(*) AS stateCount " +
                "FROM publication p " +
                "JOIN book b ON p.bookId = b.bookId " +
                "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' ");

        if(isGenreFilterActive){
            sqlQuery.append("AND bm.genre = :genre ");
        }

        if(userId!=null){
            sqlQuery.append("AND p.userId = :userId ");
        }

        sqlQuery.append("GROUP BY b.bookState");

        Query query = em.createNativeQuery(sqlQuery.toString());

        if (userId != null) {
            query.setParameter("userId", userId);
        }

        query.setParameter("publicationState", PublicationState.CURRENT.toString());
        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(isGenreFilterActive){
            query.setParameter("genre", genreFilter.toString());
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


    private int getTotalResultsByBook(Long userId, String search, boolean isGenreFilterActive, Genre genreFilter, boolean isBookStateFilterActive, BookState bookStateFilter){
        StringBuilder nativeQueryString = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "WHERE p.publicationState = :publicationState AND LOWER(bm.title) LIKE LOWER(:safeSearch) ESCAPE '\\' "
        );

        if(userId!=null){
            nativeQueryString.append("AND p.userId = :userId ");
        }

        if (isGenreFilterActive) {
            nativeQueryString.append("AND bm.genre = :genre ");
        }

        if (isBookStateFilterActive) {
            nativeQueryString.append("AND b.bookState = :bookState ");
        }

        Query query = em.createNativeQuery(nativeQueryString.toString());

        query.setParameter("publicationState", PublicationState.CURRENT.toString());

        if (userId != null) {
            query.setParameter("userId", userId);
        }

        String safeSearch = search.replace("%", "\\%").replace("_", "\\_");
        query.setParameter("safeSearch", "%" + safeSearch.toLowerCase() + "%");

        if(isGenreFilterActive){
            query.setParameter("genre", genreFilter.toString());
        }

        if(isBookStateFilterActive){
            query.setParameter("bookState", bookStateFilter.toString());
        }

        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public void deletePublication(long publicationId) {
        Publication publication = em.find(Publication.class, publicationId);
        em.remove(publication);
    }

    @Override
    public PaginatedResponse<Publication, BasicMetadata> getFavoritePublications(User user, String currentPage) {
        int page;
        try {
            page = Integer.parseInt(currentPage);
            if (page < 0) {
                page = 0;
            }
        } catch (NumberFormatException e) {
            page = 0;
        }

        String nativeQueryString = "SELECT fp.publicationid " +
                "FROM favorite_publication fp " +
                "WHERE fp.userid = :userId  " +
                "ORDER BY liked_at DESC";

        Query nativeQuery = em.createNativeQuery(nativeQueryString);
        nativeQuery.setParameter("userId", user.getUserId());
        nativeQuery.setMaxResults(PUBLICATIONS_PAGE_SIZE);
        nativeQuery.setFirstResult(page * PUBLICATIONS_PAGE_SIZE);

        @SuppressWarnings("unchecked")
        List<Long> favoritePublicationIds = nativeQuery.getResultList().stream().mapToLong(n -> ((Number) n).longValue()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (favoritePublicationIds.isEmpty()) {
            return new PaginatedResponse<>(Collections.emptyList(), new BasicMetadata(page, 0, PUBLICATIONS_PAGE_SIZE));
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
        query.setParameter("userId", user.getUserId());

        List<Publication> favoritePublications = query.getResultList();

        for(Publication publication : favoritePublications){
            setIsLikedByUser(user, publication);
        }

        return new PaginatedResponse<>(favoritePublications, new BasicMetadata(page, favoritePublications.size(), PUBLICATIONS_PAGE_SIZE));
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
    public Publication getActivePublicationById(User user, long publicationId) {
        Optional<Publication> maybePub = getPublicationByPublicationId(publicationId);
        if(user == null) {
            return maybePub.orElse(null);
        }
        if (maybePub.isPresent() && (maybePub.get().getPublicationState() == PublicationState.CURRENT ||
                (maybePub.get().getPublicationState() == PublicationState.OFFERED && Objects.equals(maybePub.get().getUser().getUserId(), user.getUserId())))) {
                return maybePub.get();
        }
        return null;
    }

    private void setIsLikedByUser(User user, Publication publication) {
        if(user == null){
            publication.setLikedByUser(false);
            return;
        }
        Query query = em.createQuery("SELECT COUNT(*) FROM FavoritePublication fp WHERE fp.publication.publicationId = :publicationId AND fp.user.userId = :userId");
        query.setParameter("publicationId", publication.getPublicationId());
        query.setParameter("userId", user.getUserId());
        publication.setLikedByUser(((Number) query.getSingleResult()).intValue() > 0);
    }

    @Transactional
    public void likePublication(long publicationId, long userId) {
        User user = em.find(User.class, userId);
        Publication publication = em.find(Publication.class, publicationId);

        /*if (user == null || publication == null) {
            throw new IllegalArgumentException("User or Publication not found");
        }*/

        TypedQuery<FavoritePublication> query = em.createQuery(
                "SELECT fp FROM FavoritePublication fp WHERE fp.publication = :publication AND fp.user = :user",
                FavoritePublication.class
        );
        query.setParameter("publication", publication);
        query.setParameter("user", user);

        List<FavoritePublication> favoritePublications = query.getResultList();

        if (!favoritePublications.isEmpty()) {
            em.remove(favoritePublications.get(0));
            publication.setLikedByUser(false);
        } else {
            FavoritePublication newFavorite = new FavoritePublication(publication, user, Timestamp.valueOf(LocalDateTime.now()));
            em.persist(newFavorite);
            publication.setLikedByUser(true);
        }
    }
}