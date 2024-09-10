package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.PublicationState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserReviewJdbcDao implements UserReviewDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<UserReview> ROWMAPPER =
            (rs, rowNum) -> new UserReview(
                    rs.getLong("userReviewId"),
                    rs.getLong("exchangeId"),
                    rs.getLong("reviewerId"),
                    rs.getLong("subjectId"),
                    rs.getString("reviewDescription"),
                    rs.getTimestamp("reviewDate"),
                    rs.getInt("userReviewRating")
            );

    public UserReviewJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<UserReview> getReviewsByUserId(long userId) {
        return jdbcTemplate.query("SELECT * FROM user_review WHERE subjectId = ?",ROWMAPPER, userId);

    }
}
