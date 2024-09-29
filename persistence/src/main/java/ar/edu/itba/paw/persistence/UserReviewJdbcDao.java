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
                    rs.getInt("userReviewRating") * 20 // Asi queda del 0 al 100%
            );

    public UserReviewJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    /*@Override
    public List<UserReview> getReviewsByUserId(long userId) {
    	List<UserReview> reviewList = jdbcTemplate.query("SELECT * FROM user_review WHERE subjectId = ?", ROWMAPPER, userId);
    	
    	return reviewList;
    }*/
    
    @Override
    public UserReview getUserReview(long exchangeId, long userId) {
    	List<UserReview> reviewList = jdbcTemplate.query("SELECT * FROM user_review WHERE exchangeId = ? AND reviewerId = ?", ROWMAPPER, exchangeId, userId);
    	
    	if (!reviewList.isEmpty()) {
    		return reviewList.getFirst();
    	}
    	
    	return null;
    }

    @Override
    public List<UserReview> getReviewsByUserId(long userId) {
        return List.of();
    }

    @Override
    public Boolean createUserReview(UserReview userReview) {
    	
    	List<UserReview> reviewList = jdbcTemplate.query("SELECT * FROM user_review WHERE exchangeId = ? AND reviewerId = ? AND subjectId = ?", ROWMAPPER, userReview.getExchangeId(), userReview.getReviewerId(), userReview.getSubjectId());
    	
    	if (!reviewList.isEmpty()) {
    		return false;
    	}
    	
        String sql = "INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating) VALUES (?, ?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, 
            userReview.getExchangeId(),
            userReview.getReviewerId(),
            userReview.getSubjectId(),
            userReview.getReviewDescription(),
            userReview.getReviewDate(),
            userReview.getUserReviewRating()
        );
        
        return rowsAffected > 0;
    }
}
