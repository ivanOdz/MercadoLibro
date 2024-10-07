package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.AuthorBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookAuthorBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookModelBadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.BookModelNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import ar.edu.itba.paw.models.utils.pagination.BookModelMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.*;

import static ar.edu.itba.paw.models.utils.Constants.BOOKS_PAGE_SIZE;

@Repository
public class BookModelJdbcDao implements BookModelDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertBookModel;
    private final SimpleJdbcInsert jdbcInsertAuthor;
    private final SimpleJdbcInsert jdbcInsertBookAuthor;

    @Autowired
    private MessageSource messageSource;

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

    @Autowired
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

        Number bookModelId;
        try {
            bookModelId = jdbcInsertBookModel.executeAndReturnKey(md);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = messageSource.getMessage("error.bookModelCreation", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new BookModelBadRequestException(errorMessage);
        }
        return bookModelId.longValue();
    }

    public List<Long> createAuthors(List<String> authors) {
        List<Long> authorsIds = new ArrayList<>();

        for (String author : authors) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("authorname", author);

            Number id;
            try{
                id = jdbcInsertAuthor.execute(parameters);
            } catch (DataIntegrityViolationException e) {
                String errorMessage = messageSource.getMessage("error.authorCreation", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
                throw new AuthorBadRequestException(errorMessage);
            }
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

            try{
                jdbcInsertBookAuthor.execute(parameters);
            } catch (DataIntegrityViolationException e) {
                String errorMessage = messageSource.getMessage("error.bookAuthorCreation", new Object[]{e.getStackTrace()},LocaleContextHolder.getLocale());
                throw new BookAuthorBadRequestException(errorMessage);
            }
        }
    }

    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        String  sqlQuery =
                "SELECT bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, (SELECT STRING_AGG(a.authorName, ', ') FROM book_author ba JOIN author a ON a.authorId = ba.authorId WHERE ba.bookModelId = bm.bookModelId) AS authors, bm.imageId AS coverId, " +
                        "AVG(br.rating) as rating, COUNT(br.rating) as ratingCount " +
                        "FROM book_model bm " +
                        "JOIN book_author ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author a ON a.authorId = ba.authorId " +
                        "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                        "WHERE bm.bookModelId = ? " +
                        "GROUP BY bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, bm.imageId";

        Optional<BookModel> bm = jdbcTemplate.query(sqlQuery, new Object[]{ bookModelId }, new int[]{Types.BIGINT}, ROW_MAPPER_BOOK_MODEL)
                .stream().findFirst();

        if(bm.isEmpty()){
            String errorMessage = messageSource.getMessage("error.bookModelNotFound", new Object[]{bookModelId}, LocaleContextHolder.getLocale());
            throw new BookModelNotFoundException(errorMessage);
        }
        return bm.get();
    }

    @Override
    public PaginatedResponse<BookModel, BookModelMetadata> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, int currentPage, SortType sortType) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, " +
                        "bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, (SELECT STRING_AGG(a.authorName, ', ') FROM book_author ba JOIN author a ON a.authorId = ba.authorId WHERE ba.bookModelId = bm.bookModelId) AS authors, bm.imageId AS coverId, AVG(br.rating) as rating, (SELECT COUNT(*) FROM book_rating br2 WHERE br2.bookModelId = bm.bookModelId) as ratingCount " +
                        "FROM book_model bm " +
                        "JOIN book b ON b.bookModelId = bm.bookModelId " +
                        "JOIN book_author ba ON ba.bookModelId = bm.bookModelId " +
                        "JOIN author a ON a.authorId = ba.authorId " +
                        "LEFT JOIN book_rating br ON bm.bookModelId = br.bookModelId " +
                        "WHERE LOWER(bm.title) LIKE LOWER(?) ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        sqlQuery.append("GROUP BY bm.bookModelId, bm.isbn, bm.title, bm.editorial, bm.description, bm.genre, bm.edition, bm.weight, bm.pages, bm.bookLanguage, bm.dimension, bm.publicationYear, bm.isPocketEdition, bm.isHardcover, coverId");

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

        int offset = currentPage * BOOKS_PAGE_SIZE;
        sqlQuery.append(" LIMIT ? OFFSET ?");

        List<BookModel> data;
        if(isGenreFilterActive) {
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ "%" + search.toLowerCase() + "%", genreFilter.getValue(), BOOKS_PAGE_SIZE, offset }, new int[]{ Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK_MODEL);
        }
        else data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ "%" + search.toLowerCase() + "%", BOOKS_PAGE_SIZE, offset }, new int[]{ Types.VARCHAR, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK_MODEL);

        int totalResults = getTotalResultsByBook(search, isGenreFilterActive, genreFilter);

        return new PaginatedResponse<>(data, new BookModelMetadata(currentPage, BOOKS_PAGE_SIZE, totalResults, search, isGenreFilterActive, genreFilter, sortType, null));

    }

    public List<GenreWrapper> getGenreQtyByBookModel(String search) {

        String sqlQuery = "SELECT bm.genre, COUNT(*) AS genreCount " +
                "FROM book_model bm " +
                "WHERE LOWER(bm.title) LIKE LOWER(?) " + "GROUP BY bm.genre";

        List<Object> params = new ArrayList<>();
        params.add("%" + search.toLowerCase() + "%");

        int[] paramTypes = new int[]{Types.VARCHAR};

        return jdbcTemplate.query(sqlQuery, params.toArray(), paramTypes, (rs, rowNum) -> {
            int genreValue = rs.getInt("genre");
            Genre genre = Genre.fromInt(genreValue);
            return new GenreWrapper(genre, rs.getInt("genreCount"));
        });
    }

    private int getTotalResultsByBook(String search, boolean isGenreFilterActive, Genre genreFilter) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM book_model bm " +
                        "WHERE LOWER(bm.title) LIKE LOWER(?) ");

        if (isGenreFilterActive) {
            sqlQuery.append("AND bm.genre = ? ");
        }

        List<Object> params = new ArrayList<>();
        params.add("%" + search.toLowerCase() + "%");

        if (isGenreFilterActive) {
            params.add(genreFilter.getValue());
        }

        Integer totalResults = jdbcTemplate.queryForObject(sqlQuery.toString(), params.toArray(), Integer.class);

        return totalResults != null ? totalResults : 0;
    }
}



















