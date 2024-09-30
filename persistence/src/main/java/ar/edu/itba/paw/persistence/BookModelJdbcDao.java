package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.Types;
import java.util.*;

import static ar.edu.itba.paw.models.utils.Constants.PAGE_SIZE;

@Repository
public class BookModelJdbcDao implements BookModelDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertBookModel;
    private final SimpleJdbcInsert jdbcInsertAuthor;
    private final SimpleJdbcInsert jdbcInsertBookAuthor;



    // package-private visibility
    static final RowMapper<BookModel> ROW_MAPPER_BOOK_MODEL = (rs, rowNum) -> new BookModel(
            rs.getLong("bookModelId"),
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("editorial"),
            rs.getString("description"),
            Genre.fromInt(rs.getInt("genre")),
            rs.getInt("edition"),
            rs.getInt("weight"),
            rs.getInt("pages"),
            Language.fromInt(rs.getInt("bookLanguage")),
            rs.getInt("dimension"),
            rs.getShort("publicationYear"),
            rs.getBoolean("isPocketEdition"),
            rs.getBoolean("isHardcover"),
            rs.getString("authors"),
            rs.getLong("coverId"),
            new Rating(rs.getDouble("rating"), rs.getInt("ratingCount"))
    );

    public BookModelJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertBookModel = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("bookmodelid")
                .withTableName("book_model");
        jdbcInsertAuthor = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("authorid")
                .withTableName("author");
        jdbcInsertBookAuthor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("book_author").usingColumns("bookmodelid", "authorid");
    }



    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, STRING_AGG(a.authorName, ', ') AS authors, bm.imageId AS coverId, " +
                        "AVG(br.rating) as rating, COUNT(br.rating) as ratingCount " +
                        "FROM book_model bm " +
                        "JOIN book_author ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author a ON a.authorId = ba.authorId " +
                        "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                        "WHERE bm.bookModelId = ? " +
                        "GROUP BY bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId"
        );

        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ bookModelId }, new int[]{Types.BIGINT}, ROW_MAPPER_BOOK_MODEL)
                .stream().findFirst().orElse(null);
    }


    /*@Override
    public BookModel addBookModel(String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language language, BookDimension dimension, Short publicationYear, boolean pocketEdition, boolean hardcover) {
        final Map<String, Object> md = new HashMap<>();
        md.put("isbn", isbn);
        md.put("title", title);
        md.put("editorial", editorial);
        md.put("description", description);
        md.put("genre", genre.getValue());
        md.put("edition", edition);
        md.put("weight", weight);
        md.put("pages", pages);
        md.put("booklanguage", language.getValue());
        md.put("dimension", dimension.getValue());
        md.put("publicationyear", publicationYear);
        md.put("ispocketedition", pocketEdition);
        md.put("ishardcover", hardcover);

        final Number modelId = jdbcInsert.executeAndReturnKey(md);

        return new BookModel(modelId.longValue(), isbn,title,editorial,description,genre,edition,weight,pages,language, dimension.getValue(), publicationYear, pocketEdition, hardcover);
    }*/

    @Override
    public List<BookModel> getBookModelByUserId(long userId) {
        return jdbcTemplate.query("SELECT bm.* FROM book b JOIN book_model bm ON b.bookModelId = bm.bookModelId JOIN users u ON b.ownerId = u.userId WHERE b.ownerId = ?",
                new Object[] { userId }, new int[] {Types.BIGINT}, ROW_MAPPER_BOOK_MODEL);
    }

    @Override
    public List<BookModel> getAllBookModel(String search, int genreFilter) {
        return jdbcTemplate.query("SELECT * FROM book_model WHERE LOWER(title) LIKE LOWER(?) AND (32 = ? OR genre = ?) ",
                new Object[]{ "%" + search.toLowerCase() + "%", genreFilter, genreFilter }, new int[]{Types.VARCHAR, Types.INTEGER, Types.INTEGER}, ROW_MAPPER_BOOK_MODEL);
    }

    @Override
    public Rating getRatingByBookModelId(long bookModelId) {
        return jdbcTemplate.queryForObject("SELECT AVG(rating) AS average_rating, COUNT(rating) AS total_ratings FROM book WHERE bookModelId = ? AND rating IS NOT NULL", new Object[]{bookModelId}, (rs, rowNum) ->
            new Rating(rs.getDouble("average_rating"), rs.getInt("total_ratings"))
        );
    }

    @Override
    public List<BookModel> getFilteredSortedOrderedModelBooksByPage(String search, boolean isGenreFilterActive, Genre genreFilter, int pageIndex, SortType sortType) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, STRING_AGG(a.authorName, ', ') AS authors, bm.imageId AS coverId, AVG(br.rating) as rating, COUNT(br.rating) as ratingCount " +
                        "FROM book_model bm " +
                        "JOIN book b ON b.bookModelId = bm.bookModelId " +
                        "JOIN book_author ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author a ON a.authorId = ba.authorId " +
                        "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                        "WHERE LOWER(bm.title) LIKE LOWER(?) ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        sqlQuery.append("GROUP BY bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId");

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
            default:
                sqlQuery.append(" ORDER BY title DESC");
        }

        int offset = pageIndex * PAGE_SIZE;
        sqlQuery.append(" LIMIT ? OFFSET ?");

        if(isGenreFilterActive) {
            return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ "%" + search.toLowerCase() + "%", genreFilter.getValue(), PAGE_SIZE, offset }, new int[]{ Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK_MODEL);
        }
        return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ "%" + search.toLowerCase() + "%", PAGE_SIZE, offset }, new int[]{ Types.VARCHAR, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK_MODEL);

    }

    @Override
    public long createBookModel(String isbn, String title, String publisher, String description, Genre genre, int edition, Short publicationYear, boolean isHardcover, boolean isPocketEdition, BookDimension dimension, Language language, int pages, int weight, long bookCoverId) {
        final Map<String, Object> md = new HashMap<>();
        md.put("isbn", isbn);
        md.put("title", title);
        md.put("editorial", publisher);
        md.put("description", description);
        md.put("genre", genre.getValue());
        md.put("edition", edition);
        md.put("weight", weight);
        md.put("pages", pages);
        md.put("booklanguage", language.getValue());
        md.put("dimension", dimension.getValue());
        md.put("publicationyear", publicationYear);
        md.put("ispocketedition", isPocketEdition);
        md.put("ishardcover", isHardcover);
        md.put("imageid", bookCoverId);

        final Number bookModelId = jdbcInsertBookModel.executeAndReturnKey(md);

        return bookModelId.longValue();

    }

    public List<Long> createAuthors(List<String> authors) {
        List<Long> authorsIds = new ArrayList<>();

        for (String author : authors) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("authorname", author);

            Number id = jdbcInsertAuthor.executeAndReturnKey(parameters);
            authorsIds.add(id.longValue());
        }
        return authorsIds;
    }

    @Override
    public void createBookAuthors(List<Long> authorsIds, long bookModelId) {
        for (Long authorId : authorsIds) {
            Map<String, Long> parameters = new HashMap<>();
            parameters.put("bookmodelid", bookModelId);
            parameters.put("authorid", authorId);

            jdbcInsertBookAuthor.execute(parameters);
        }
    }
}

















