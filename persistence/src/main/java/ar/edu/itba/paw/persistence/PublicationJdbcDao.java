package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.sql.Timestamp;
import java.sql.Types;

import static ar.edu.itba.paw.persistence.BookJdbcDao.ROW_MAPPER_BOOK;

@Repository
public class PublicationJdbcDao implements PublicationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final int PAGE_SIZE = 21;

    static final RowMapper<Publication> ROW_MAPPER_PUBLICATION =
            (rs, rowNum) -> {
                long id = rs.getLong("publicationId");
                Book book = ROW_MAPPER_BOOK.mapRow(rs, rowNum);
                PublicationState publicationState = PublicationState.fromInt(rs.getInt("publicationState"));
                Timestamp dateTime = rs.getTimestamp("publicationDatetime");
                Location location = new Location(rs.getLong("locationId"), rs.getString("locationString"));
                return new Publication(id, book, publicationState, dateTime, location);
            };

    public PublicationJdbcDao(final DataSource ds) {
        jdbcTemplate =  new JdbcTemplate(ds);;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("publicationid")
                .withTableName("publication");
    }


//    @Override
//    public List<Publication> getAllPublications() {
//        return jdbcTemplate.query("SELECT * FROM publication", ROWMAPPERPUBLICATIONS);
//    }
//
//    @Override
//    public Optional<Publication> getPublicationById(long pubId) {
//        return jdbcTemplate.query("SELECT * FROM publication WHERE publicationId = ?", new Object[]{ pubId },
//                new int[]{ Types.BIGINT }, ROWMAPPERPUBLICATIONS).stream().findFirst();
//    }
//
//    @Override
//    public List<Publication> getAllPublicationsFilteredBy(String search, int bookStateFilter, int genreFilter,long userId) {
//
//        return jdbcTemplate.query(
//                "SELECT * FROM publication WHERE publicationState = ? AND userId <> ? AND bookId IN (SELECT bookId FROM book WHERE (? = 6 OR bookstate = ?) AND bookModelId IN (SELECT bookModelId FROM book_model WHERE LOWER(title) LIKE LOWER(?) AND (? = 32 OR genre = ?)))",
//                new Object[]{ PublicationState.CURRENT.getValue(), userId, bookStateFilter, bookStateFilter, "%" + search.toLowerCase() + "%", genreFilter, genreFilter },
//                new int[]{ Types.INTEGER, Types.BIGINT, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER },
//                ROWMAPPERPUBLICATIONS
//        );
//    }

    @Override
    public void terminatePublication(long pubId) {
        jdbcTemplate.update("UPDATE publication SET publicationState = ? WHERE publicationId = ?",PublicationState.TERMINATED.getValue(), pubId);
    }

//
//    @Override
//    public Optional<Publication> getPublicationStateByBookId(long bookId) {
//        return jdbcTemplate.query("SELECT * FROM publication WHERE bookId = ? ORDER BY publicationDatetime DESC LIMIT 1", new Object[]{ bookId }, new int[]{ Types.BIGINT }, ROWMAPPERPUBLICATIONS).stream().findFirst();
//
//    }

    @Override
    public long createPublication(long bookId, long userId, long locationId, PublicationState publicationState) {
        final Map<String, Object> md = new HashMap<>();
        md.put("bookId", bookId);
        md.put("userId", userId);
        md.put("publicationState", publicationState.getValue());
        md.put("publicationDatetime", new Timestamp(new Date().getTime()));
        md.put("locationId", locationId);

        return jdbcInsert.executeAndReturnKey(md).longValue();
    }

    @Override
        public List<Publication> getFilteredSortedOrderedPublicationsByPageExcludingUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT p.publicationId, " +
                    "p.publicationState, l.locationId, l.locationString, p.publicationDatetime," +

                    //book
                    "b.bookId, ARRAY_AGG(i.imageId ORDER BY bi.imageOrder) AS images, " +
                    "b.bookState, b.exchangesQty," +
                    //book_model
                    "bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                    "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId, AVG(br.rating) as rating, COUNT(br.rating) as ratingCount, " + "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, " +
                    "(SELECT STRING_AGG(a.authorName, ', ') " +
                    " FROM book_author ba " +
                    " JOIN author a ON a.authorId = ba.authorId " +
                    " WHERE ba.bookModelId = bm.bookModelId) AS authors "+
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
                    "LEFT JOIN (SELECT bb.bookModelId, AVG(bb.rating) AS rating, COUNT(bb.rating) AS ratingCount " +
                    "FROM book_rating bb " +
                    "GROUP BY bb.bookModelId) avgRatings ON avgRatings.bookModelId = bm.bookModelId " +
                    "WHERE u.userId <> ? AND p.publicationState = ? AND LOWER(bm.title) LIKE LOWER(?) "
        );

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        sqlQuery.append("GROUP BY u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, p.publicationId, p.publicationState, l.locationId, l.locationString, p.publicationDatetime,b.bookId, b.bookState, b.exchangesQty, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId");

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

        int offset = pageIndex * PAGE_SIZE;
        sqlQuery.append(" LIMIT ? OFFSET ?");

        if(isGenreFilterActive && isBookStateFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%", genreFilter.getValue(), bookStateFilter.getValue(), PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER  }, ROW_MAPPER_PUBLICATION);
        }
        if(isGenreFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%", genreFilter.getValue(), PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_PUBLICATION);
        }
        if(isBookStateFilterActive){
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%", bookStateFilter.getValue(), PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_PUBLICATION);
        }
        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%", PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_PUBLICATION);
    }

    @Override
    public Publication getPublicationByPublicationId(long publicationId) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT p.publicationId, " +
                "p.publicationState, l.locationId, l.locationString, p.publicationDatetime," +

                //book
                "b.bookId, ARRAY_AGG(bi.imageId ORDER BY bi.imageOrder) AS images, " +
                "b.bookState, b.exchangesQty," +
                //book_model
                "bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                "(SELECT STRING_AGG(a.authorName, ', ') " +
                " FROM book_author ba " +
                " JOIN author a ON a.authorId = ba.authorId " +
                " WHERE ba.bookModelId = bm.bookModelId) AS authors, "+
                "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId, AVG(br.rating) as rating, COUNT(br.rating) as ratingCount, " +
                "u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified " +

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
                "LEFT JOIN (SELECT bb.bookModelId, AVG(bb.rating) AS rating, COUNT(bb.rating) AS ratingCount " +
                "FROM book bb " +
                "GROUP BY bb.bookModelId) avgRatings ON avgRatings.bookModelId = bm.bookModelId " +
                "WHERE p.publicationId = ? AND p.publicationState = ? " +
                "GROUP BY u.userId, u.username, u.mail, u.password, u.imageId, u.verificationCode, u.isVerified, p.publicationId, p.publicationState, l.locationId, l.locationString, p.publicationDatetime, b.bookId, b.bookState, b.exchangesQty, bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId");

        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ publicationId, PublicationState.CURRENT.getValue() }, new int[]{ Types.BIGINT, Types.INTEGER }, ROW_MAPPER_PUBLICATION).stream().findFirst().orElse(null);
    }
}









