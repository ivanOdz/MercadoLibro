package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.base.BadRequestException;
import ar.edu.itba.paw.interfaces.exceptions.base.NotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.GenreService;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.PageInfo;
import ar.edu.itba.paw.models.PaginatedResponse;
import ar.edu.itba.paw.models.utils.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    private final GenreService genreService;

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

    public BookModelJdbcDao(final DataSource ds, GenreService genreService) {
        jdbcTemplate = new JdbcTemplate(ds);
        this.genreService = genreService;
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

        try {
            final Number bookModelId = jdbcInsertBookModel.executeAndReturnKey(md);
            return bookModelId.longValue();
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Data integrity violation: One or more fields contain invalid values, or the book model already exists.");
        }
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

        BookModel bookModel = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ bookModelId }, new int[]{Types.BIGINT}, ROW_MAPPER_BOOK_MODEL)
                .stream().findFirst().orElse(null);

        if (bookModel == null) {
            throw new NotFoundException("Book model with ID " + bookModelId + " not found.");
        }
        return bookModel;
    }

    @Override
    public PaginatedResponse<BookModel> getPaginatedBookModels(String search, boolean isGenreFilterActive, Genre genreFilter, int currentPage, SortType sortType) {
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

        int offset = currentPage * PAGE_SIZE;
        sqlQuery.append(" LIMIT ? OFFSET ?");

        List<BookModel> data;
        if(isGenreFilterActive) {
            data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ "%" + search.toLowerCase() + "%", genreFilter.getValue(), PAGE_SIZE, offset }, new int[]{ Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK_MODEL);
        }
        else data = jdbcTemplate.query(sqlQuery.toString(), new Object[]{ "%" + search.toLowerCase() + "%", PAGE_SIZE, offset }, new int[]{ Types.VARCHAR, Types.INTEGER, Types.INTEGER }, ROW_MAPPER_BOOK_MODEL);

        List<GenreWrapper> genreWrapperList = getGenreQtyByBook(search);

        int totalResults = getTotalResultsByBook(search, isGenreFilterActive, genreFilter);

        return new PaginatedResponse<>(data, new PageInfo(search, false, isGenreFilterActive, genreFilter, null, sortType, genreWrapperList, null, currentPage, totalResults));

    }

    private List<GenreWrapper> getGenreQtyByBook(String search) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT bm.genre, COUNT(*) AS genreCount " +
                        "FROM book_model bm " +
                        "WHERE LOWER(bm.title) LIKE LOWER(?) ");

        sqlQuery.append("GROUP BY bm.genre");

        List<Object> params = new ArrayList<>();
        params.add("%" + search.toLowerCase() + "%");

        int[] paramTypes;
        paramTypes = new int[]{Types.VARCHAR};

        Map<Genre, Integer> resultByGenreMap = new HashMap<>();
        jdbcTemplate.query(sqlQuery.toString(), params.toArray(), paramTypes, (rs, rowNum) -> {
            int genreValue = rs.getInt("genre");
            Genre genre = Genre.fromInt(genreValue);
            resultByGenreMap.put(genre, rs.getInt("genreCount"));
            return null;
        });

        List<GenreWrapper> genreWrappers = new ArrayList<>();
        for (Genre genre : Genre.values()) {
            genreWrappers.add(new GenreWrapper(genre, genreService.getGenreDisplayName(genre), resultByGenreMap.getOrDefault(genre, 0)));
        }

        return genreWrappers;
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



















