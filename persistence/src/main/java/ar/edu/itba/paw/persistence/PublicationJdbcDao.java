package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.PublicationBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.PublicationNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.ItemFilterMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.sql.Timestamp;
import java.sql.Types;

import static ar.edu.itba.paw.models.utils.Constants.PUBLICATIONS_PAGE_SIZE;
import static ar.edu.itba.paw.persistence.BookJdbcDao.ROW_MAPPER_BOOK;

@Repository
public class PublicationJdbcDao implements PublicationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    private MessageSource messageSource;

    static final RowMapper<Publication> ROW_MAPPER_PUBLICATION =
            (rs, rowNum) -> {
                long id = rs.getLong("publicationId");
                Book book = ROW_MAPPER_BOOK.mapRow(rs, rowNum);
                PublicationState publicationState = PublicationState.fromInt(rs.getInt("publicationState"));
                Timestamp dateTime = rs.getTimestamp("publicationDatetime");
                Location location = new Location(rs.getLong("locationId"), rs.getString("locationString"));
                return new Publication(id, book, publicationState, dateTime, location);
            };

    @Autowired
    public PublicationJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("publicationid")
                .withTableName("publication");
    }

    @Override
    public long createPublication(long bookId, long userId, long locationId, PublicationState publicationState) {
        final Map<String, Object> md = new HashMap<>();
        md.put("bookId", bookId);
        md.put("userId", userId);
        md.put("publicationState", publicationState.getValue());
        md.put("publicationDatetime", new Timestamp(new Date().getTime()));
        md.put("locationId", locationId);

        long publicationId;
        try {
            publicationId = jdbcInsert.executeAndReturnKey(md).longValue();
        } catch (DataIntegrityViolationException e) {
            String message = messageSource.getMessage("error.createPublication", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new PublicationBadRequestException(message);
        }
        return publicationId;
    }

    @Override
    public void terminatePublication(long pubId) {
        try {
            jdbcTemplate.update("UPDATE publication SET publicationState = ? WHERE publicationId = ?", PublicationState.TERMINATED.getValue(), pubId);
        } catch (DataIntegrityViolationException e) {
            String message = messageSource.getMessage("error.terminatePublicationBadRequest", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new PublicationBadRequestException(message);
        }
    }

    @Override
    public Publication getPublicationByPublicationId(long publicationId) {
        String sqlQuery =
                "SELECT p.publicationId, " +
                        "p.publicationState, l.locationId, l.locationString, p.publicationDatetime," +
                        // -- book
                        "b.bookId, ARRAY_AGG(bi.imageId ORDER BY bi.imageOrder) AS images, " +
                        "b.bookState, b.exchangesQty," +
                        "CASE " +
                        "WHEN NOT EXISTS (SELECT 1 FROM publication p2 WHERE p2.bookId = b.bookId) THEN TRUE " +
                        "WHEN NOT EXISTS (SELECT 1 FROM exchange e2 JOIN publication p2 ON e2.offererPubId = p2.publicationId OR e2.requesterPubId = p2.publicationId " +
                        "WHERE p2.bookId = b.bookId AND e2.exchangeState = ?) THEN TRUE " +
                        "ELSE FALSE " +
                        "END AS available, " +
                        // -- book_model
                        "bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "(SELECT STRING_AGG(a.authorName, ', ') " +
                        " FROM book_author ba " +
                        " JOIN author a ON a.authorId = ba.authorId " +
                        " WHERE ba.bookModelId = bm.bookModelId) AS authors, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId as coverId, AVG(br.rating) as rating, COUNT(br.rating) as ratingCount, " +
                        "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language " +

                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN users u ON b.ownerId = u.userId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                        "JOIN book_author ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author a ON a.authorId = ba.authorId " +
                        "LEFT JOIN book_image bi ON bi.bookId = b.bookId " +
                        "LEFT JOIN image i ON bm.imageId = i.imageId " +
                        "JOIN location l ON p.locationId = l.locationId " +
                        // "-- LEFT JOIN (SELECT bb.bookModelId, AVG(bb.rating) AS rating, COUNT(bb.rating) AS ratingCount " +
                        // "-- FROM book bb " +
                        // "-- GROUP BY bb.bookModelId) avgRatings ON avgRatings.bookModelId = bm.bookModelId " +
                        "WHERE p.publicationId = ? AND p.publicationState = ? " +
                        "GROUP BY u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language , p.publicationId, p.publicationState, l.locationId, l.locationString, p.publicationDatetime, b.bookId, b.bookState, b.exchangesQty, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, coverId";

        Optional<Publication> publication = jdbcTemplate.query(sqlQuery, new Object[]{ExchangeState.ACCEPTED.getValue(), publicationId, PublicationState.CURRENT.getValue()}, new int[]{Types.INTEGER, Types.BIGINT, Types.INTEGER}, ROW_MAPPER_PUBLICATION).stream().findFirst();

        if (publication.isEmpty()) {
            String message = messageSource.getMessage("error.publicationNotFound", new Object[]{publicationId}, LocaleContextHolder.getLocale());
            throw new PublicationNotFoundException(message);
        }

        return publication.get();
    }

    @Override
    public PaginatedResponse<Publication, ItemFilterMetadata> getPaginatedPublications(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, SortType sortType, int currentPage) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT p.publicationId, " +
                        "p.publicationState, l.locationId, l.locationString, p.publicationDatetime," +

                        //book
                        "b.bookId, ARRAY_AGG(i.imageId ORDER BY bi.imageOrder) AS images, " +
                        "b.bookState, b.exchangesQty," +
                        //book_model
                        "bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId AS coverId, AVG(br.rating) as rating, COUNT(br.rating) as ratingCount, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, " +
                        "(SELECT STRING_AGG(a.authorName, ', ') " +
                        " FROM book_author ba " +
                        " JOIN author a ON a.authorId = ba.authorId " +
                        " WHERE ba.bookModelId = bm.bookModelId) AS authors, " +
                        "CASE " +
                        "WHEN NOT EXISTS (SELECT 1 FROM publication p2 WHERE p2.bookId = b.bookId) THEN TRUE " +
                        "WHEN NOT EXISTS (SELECT 1 FROM exchange e2 JOIN publication p2 ON e2.offererPubId = p2.publicationId OR e2.requesterPubId = p2.publicationId " +
                        "WHERE p2.bookId = b.bookId AND e2.exchangeState = ?) THEN TRUE " +
                        "ELSE FALSE " +
                        "END AS available " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN users u ON b.ownerId = u.userId " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                        "JOIN book_author ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author a ON a.authorId = ba.authorId " +
                        "LEFT JOIN book_image bi ON bi.bookId = b.bookId " +
                        "LEFT JOIN image i ON bm.imageId = i.imageId " +
                        "JOIN location l ON p.locationId = l.locationId " +
                        "WHERE p.publicationState = ? AND LOWER(bm.title) LIKE LOWER(?) "
        );

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        sqlQuery.append("GROUP BY available, u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, u.language, p.publicationId, p.publicationState, l.locationId, l.locationString, p.publicationDatetime,b.bookId, b.bookState, b.exchangesQty, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId");

        switch (sortType) {
            case RATING_ASCENDING:
                sqlQuery.append(" ORDER BY rating ASC");
                break;
            case RATING_DESCENDING:
                sqlQuery.append(" ORDER BY rating DESC");
                break;
            case BOOK_NAME_ASCENDING:
                sqlQuery.append(" ORDER BY title ASC");
                break;
            case BOOK_NAME_DESCENDING:
                sqlQuery.append(" ORDER BY title DESC");
                break;
            case PUBLICATION_DATE_DESCENDING:
                sqlQuery.append(" ORDER BY publicationDatetime DESC");
                break;
            default:
                sqlQuery.append(" ORDER BY publicationDatetime ASC");
        }

        int offset = currentPage * PUBLICATIONS_PAGE_SIZE;
        sqlQuery.append(" LIMIT ? OFFSET ?");

        List<Publication> data;
        if (isGenreFilterActive && isBookStateFilterActive) {
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%", genreFilter.getValue(), bookStateFilter.getValue(), PUBLICATIONS_PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_PUBLICATION);
        } else if (isGenreFilterActive) {
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%", genreFilter.getValue(), PUBLICATIONS_PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_PUBLICATION);
        } else if (isBookStateFilterActive) {
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%", bookStateFilter.getValue(), PUBLICATIONS_PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_PUBLICATION);
        } else
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ExchangeState.ACCEPTED.getValue(), PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%", PUBLICATIONS_PAGE_SIZE, offset}, new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_PUBLICATION);

        int totalResults = getTotalResultsByBook(search, isGenreFilterActive, genreFilter, isBookStateFilterActive, bookStateFilter);

        return new PaginatedResponse<>(data, new ItemFilterMetadata(currentPage, PUBLICATIONS_PAGE_SIZE, totalResults, search, isGenreFilterActive, genreFilter, sortType, null, isBookStateFilterActive, bookStateFilter, null));
    }

    @Override
    public int getPublicationCountByUserId(long userId) {
        String sql = "SELECT COUNT(*) FROM publication WHERE userId = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
    }

    @Override
    public List<BookStateWrapper> getBookStateQtyByPublication(String search, boolean isGenreFilterActive, Genre genreFilter) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT b.bookState, COUNT(*) AS stateCount " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE p.publicationState = ? AND LOWER(bm.title) LIKE LOWER(?) ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        sqlQuery.append("GROUP BY b.bookState");

        List<Object> params = new ArrayList<>();
        params.add(PublicationState.CURRENT.getValue());
        params.add("%" + search.toLowerCase() + "%");

        if (isGenreFilterActive) {
            params.add(genreFilter.getValue());
        }

        int[] paramTypes;
        if (isGenreFilterActive) {
            paramTypes = new int[]{Types.INTEGER, Types.VARCHAR, Types.INTEGER};
        } else {
            paramTypes = new int[]{Types.INTEGER, Types.VARCHAR};
        }

        return jdbcTemplate.query(sqlQuery.toString(), params.toArray(), paramTypes, (rs, rowNum) -> {
            int stateValue = rs.getInt("bookState");
            BookState bookState = BookState.fromInt(stateValue);
            return new BookStateWrapper(bookState, rs.getInt("stateCount"));
        });
    }

    @Override
    public List<GenreWrapper> getGenreQtyByPublication(String search, boolean isBookStateFilterActive, BookState bookStateFilter) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT bm.genre, COUNT(*) AS genreCount " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE p.publicationState = ? AND LOWER(bm.title) LIKE LOWER(?) ");

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        sqlQuery.append("GROUP BY bm.genre");

        List<Object> params = new ArrayList<>();
        params.add(PublicationState.CURRENT.getValue());
        params.add("%" + search.toLowerCase() + "%");

        if (isBookStateFilterActive) {
            params.add(bookStateFilter.getValue());
        }

        int[] paramTypes;
        if (isBookStateFilterActive) {
            paramTypes = new int[]{Types.INTEGER, Types.VARCHAR, Types.INTEGER};
        } else {
            paramTypes = new int[]{Types.INTEGER, Types.VARCHAR};
        }

        return jdbcTemplate.query(sqlQuery.toString(), params.toArray(), paramTypes, (rs, rowNum) -> {
            int genreValue = rs.getInt("genre");
            Genre genre = Genre.fromInt(genreValue);
            return new GenreWrapper(genre, rs.getInt("genreCount"));
        });
    }

    private int getTotalResultsByBook(String search, boolean isGenreFilterActive, Genre genreFilter,
                                      boolean isBookStateFilterActive, BookState bookStateFilter) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN book_model bm ON b.bookModelId = bm.bookModelId " +
                        "WHERE p.publicationState = ? AND LOWER(bm.title) LIKE LOWER(?) ");

        // Agregar el filtro de estado del libro si está activo
        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        // Agregar el filtro por género si está activo
        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        // Parámetros para la consulta
        List<Object> params = new ArrayList<>();
        params.add(PublicationState.CURRENT.getValue());  // Estado de publicación actual
        params.add("%" + search.toLowerCase() + "%");     // Filtro de búsqueda (coincidencia parcial)

        // Añadir el filtro del estado del libro si está activo
        if (isBookStateFilterActive) {
            params.add(bookStateFilter.getValue());
        }

        // Añadir el filtro de género si está activo
        if (isGenreFilterActive) {
            params.add(genreFilter.getValue());
        }

        // Ejecutar la consulta y obtener el total de resultados
        Integer totalResults = jdbcTemplate.queryForObject(sqlQuery.toString(), params.toArray(), Integer.class);

        // Devolver 0 si totalResults es null
        return totalResults != null ? totalResults : 0;
    }

}









