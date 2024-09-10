package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PublicationDao;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class PublicationJdbcDao implements PublicationDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Publication> ROWMAPPERPUBLICATIONS =
            (rs, rowNum) -> new Publication(
                    rs.getLong("publicationId"),
                    rs.getInt("bookId"),
                    rs.getInt("userId"),
                    PublicationState.fromInt(rs.getInt("publicationState")),
                    rs.getTimestamp("publicationDatetime"),
                    rs.getLong("locationId")
            );

    public PublicationJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<Publication> getAllPublications() {
        return jdbcTemplate.query("SELECT * FROM publication", ROWMAPPERPUBLICATIONS);
    }

    @Override
    public List<Publication> getAllPublicationsAvailable() {
        return jdbcTemplate.query("SELECT * FROM publication WHERE publicationState = ?", ROWMAPPERPUBLICATIONS, PublicationState.CURRENT.getValue());
    }

    @Override
    public Optional<Publication> getPublicationById(long pubId) {
        return jdbcTemplate.query("SELECT * FROM publication WHERE publicationId = ?", new Object[]{ pubId },
                new int[]{ Types.BIGINT }, ROWMAPPERPUBLICATIONS).stream().findFirst();
    }

    @Override
    public List<Publication> getAllPublicationsFilteredBy(String search) {
        if (search.compareTo("") == 0) {
            return getAllPublicationsAvailable();
        }
        return jdbcTemplate.query(
                "SELECT * FROM publication WHERE publicationState = ? AND bookId IN (SELECT bookId from book WHERE bookModelId IN (SELECT bookModelId FROM book_model WHERE LOWER(title) LIKE LOWER(?))",
                new Object[]{ PublicationState.CURRENT.getValue(), "%" + search.toLowerCase() + "%" }, new int[]{ Types.INTEGER, Types.VARCHAR }, ROWMAPPERPUBLICATIONS);
    }

    @Override
    public void terminatePublication(long pubId) {
        jdbcTemplate.update("UPDATE publication SET publicationState = ? WHERE publicationId = ?",PublicationState.TERMINATED.getValue(), pubId);
    }
}









