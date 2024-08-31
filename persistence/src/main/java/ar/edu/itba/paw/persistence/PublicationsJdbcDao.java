package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.Publications;
import ar.edu.itba.paw.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
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
    public Optional<Publications> getAllPublications() {
        List<Publication> publicationsList = jdbcTemplate.query("SELECT * FROM publication", ROWMAPPERPUBLICATIONS);

        if (publicationsList.isEmpty()) {
            return Optional.empty();
        } else {
            Publications publications = new Publications(publicationsList);
            return Optional.of(publications);
        }
    }
}










