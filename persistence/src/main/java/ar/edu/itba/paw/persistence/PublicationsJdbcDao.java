package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.persistence.PublicationsDao;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.Publications;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PublicationsJdbcDao implements PublicationsDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Publication> ROWMAPPERPUBLICATIONS =
            (rs, rowNum) -> new Publication(
                    rs.getLong("publicationId"),
                    rs.getInt("bookId"),
                    rs.getInt("userId"),
                    rs.getInt("publicationState"),
                    rs.getString("location")
            );

    public PublicationsJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Publications getAllPublications() {
        List<Publication> publicationsList = jdbcTemplate.query("SELECT * FROM publication", ROWMAPPERPUBLICATIONS);
        return new Publications(publicationsList);
    }

    @Override
    public Optional<Publication> getPublicationById(long pubId) {
        return jdbcTemplate.query("SELECT * FROM publication WHERE publicationId = ?", new Object[]{ pubId },
                new int[]{ Types.BIGINT }, ROWMAPPERPUBLICATIONS).stream().findFirst();
    }

    @Override
    public Publications getAllPublicationsFilteredBy(String search) {
        if(search.compareTo("") == 0) {
            return getAllPublications();
        }
        // Matecheo exacto del titulo del libro con lo que se busca.
        List<Publication> publicationsList = jdbcTemplate.query(
                "SELECT * FROM publication WHERE bookId IN (SELECT bookId from books WHERE title = ?)",
                new Object[]{ search }, new int[]{ Types.VARCHAR }, ROWMAPPERPUBLICATIONS);
        return new Publications(publicationsList);
    }
}









