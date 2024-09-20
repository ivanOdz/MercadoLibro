package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.PublicationCard;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.SortType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PublicationJdbcDao implements PublicationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final int PAGE_SIZE = 20;


    private static final RowMapper<PublicationCard> ROWMAPPER_PUBLICATIONCARD =
            (rs, rowNum) -> new PublicationCard(
                    rs.getLong("publicationId"),
                    rs.getString("title"),
                    rs.getLong("imageId"),
                    rs.getString("authors"), // deberia ser un List<String>
                    rs.getFloat("averageRating"),
                    rs.getTimestamp("publicationDatetime"),
                    Genre.fromInt(rs.getInt("genre")),
                    BookState.fromInt(rs.getInt("bookState"))
            );


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
    public List<PublicationCard> getFilteredSortedOrderedPublicationsByPageExcludingUser(String search, boolean isBookStateFilterActive, BookState bookStateFilter, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, long userId, SortType sortType) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT p.publicationId, bm.title, i.imageId, STRING_AGG(a.authorName, ', ') AS authors, AVG(b.rating) AS averageRating, p.publicationDatetime, bm.genre, b.bookState " +
                    "FROM publication p " +
                        "JOIN book b ON p.bookId = b.bookId " +
                        "JOIN users u ON p.userid = u.userid " +
                        "JOIN book_model bm ON bm.bookModelId = b.bookModelId " +
                        "JOIN book_author ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author a ON a.authorId = ba.authorId " +
                        "JOIN book_image bi ON bi.bookId = b.bookId " +
                        "JOIN image i ON bi.imageId = i.imageId " +
                        "WHERE bi.imageOrder = 0 AND u.userid <> ? AND p.publicationState = ? ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        if (isBookStateFilterActive) {
            sqlQuery.append("AND b.bookState = ? ");
        }

        sqlQuery.append("GROUP BY p.publicationId, bm.title, i.imageId, p.publicationDatetime, bm.genre, b.bookState, p.publicationState");

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
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, PublicationState.CURRENT.getValue(), genreFilter, bookStateFilter, PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER  }, ROWMAPPER_PUBLICATIONCARD);
        }
        if(isGenreFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, PublicationState.CURRENT.getValue(), genreFilter, PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROWMAPPER_PUBLICATIONCARD);
        }
        if(isBookStateFilterActive){
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, PublicationState.CURRENT.getValue(), bookStateFilter, PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROWMAPPER_PUBLICATIONCARD);
        }
        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId, PublicationState.CURRENT.getValue(), PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROWMAPPER_PUBLICATIONCARD);
    }
}









