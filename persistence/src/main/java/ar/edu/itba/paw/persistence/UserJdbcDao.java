package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserReview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class UserJdbcDao implements UserDao {

    static final RowMapper<User> ROW_MAPPER_USER =
            (rs, rowNum) -> new User(rs.getLong("userId"),
                                     rs.getString("username"),
                                     rs.getString("mail"),
                                     rs.getString("password"),
                                     rs.getLong("imageId"),
                                     rs.getInt("verificationCode"),
                                     rs.getBoolean("isVerified"),
                                     rs.getString("language"));
/*
    private static final RowMapper<UserReview> ROWMAPPER_USER_REVIEW =
            (rs, rowNum) -> new UserReview(rs.getLong("userReviewId"),
                                           rs.getLong("exchangeId"),
                                           rs.getLong("reviewerId"),
                                           rs.getLong("subjectId"),
                                           rs.getString("reviewDescription"),
                                           rs.getTimestamp("reviewDate"),
                                           rs.getInt("userReviewRating"));

    private static final RowMapper<Double> ROWMAPPER_USER_REVIEW_RATING =
            (rs, rowNum) -> rs.getDouble("userReviewRating");
*/

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static int PAGE_SIZE = 21;

    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("userid")
                .withTableName("users");
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userId = ?", new Object[]{ id },
                new int[]{ Types.BIGINT }, ROW_MAPPER_USER).stream().findFirst();
    }

    @Override
    public Optional<User> find(String mail) {
        return jdbcTemplate.query("SELECT * FROM users WHERE mail = ?", new Object[]{ mail },
        		new int[]{ Types.VARCHAR }, ROW_MAPPER_USER).stream().findFirst();
    }
    
    @Override
    public boolean updateUsername(long userId, String newUsername) {
    	    	
    	int rowsAffected = jdbcTemplate.update("UPDATE users SET userName = ? WHERE userId = ? AND NOT EXISTS (SELECT * FROM users WHERE userName = ?)", new Object[]{ newUsername, userId, newUsername }, new int[]{ Types.VARCHAR, Types.BIGINT, Types.VARCHAR });
    	
    	return rowsAffected >= 1;
    }

    @Override
    public Optional<User> getUserToVerify(int verificationCode) {
        return jdbcTemplate.query("SELECT * FROM users WHERE verificationcode = ?", new Object[]{ verificationCode },
                new int[]{ Types.INTEGER }, ROW_MAPPER_USER).stream().findFirst();
    }
/*
    @Override
    public List<UserReview> getReviewsByUserId(long userId, int pageIndex) {
        int offset = PAGE_SIZE * pageIndex;
        return jdbcTemplate.query("SELECT * FROM user_review WHERE subjectId = ? ORDER BY reviewdate DESC LIMIT ? OFFSET ?",
                new Object[]{ userId, PAGE_SIZE, offset }, new int[]{ Types.BIGINT, Types.INTEGER, Types.INTEGER }, ROWMAPPER_USER_REVIEW);
    }

    public Double getUserRating(long userId) {
        String sql = "SELECT AVG(userReviewRating) AS userReviewRating FROM user_review WHERE subjectId = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new int[]{ Types.BIGINT }, ROWMAPPER_USER_REVIEW_RATING).stream().findFirst().get();
    }
*/
    @Override
    public String getUserLanguage(long userId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userId = ?", new Object[]{ userId },
                new int[]{ Types.BIGINT }, ROW_MAPPER_USER).stream().findFirst().get().getLanguage();
    }

    @Override
    public void setUserLanguage(long userId, String language) {
        jdbcTemplate.update("UPDATE users SET language = ? WHERE userId = ?", new Object[]{ language, userId },
                new int[]{ Types.VARCHAR, Types.BIGINT });
    }


    @Override
    public User createUser(String username, String mail, String password, String language, int verificationCode) {
        Optional<User> user = find(mail);

        if (user.isPresent()) {
            return null;
        }



        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("mail", mail);
        userData.put("password", password);
        userData.put("imageId", null); // Permite null
        userData.put("verificationCode", verificationCode);
        userData.put("isVerified", false);
        userData.put("language", language);

        final Number userId = jdbcInsert.executeAndReturnKey(userData);


        return new User(userId.longValue(), username, mail, password, null, verificationCode, false, language);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ? ", new Object[]{ username }, new int[] {Types.VARCHAR}, ROW_MAPPER_USER).stream().findFirst();
    }

    @Override
    public void verifyUser(int verificationCode) {
        jdbcTemplate.update("UPDATE users SET isVerified = ? WHERE verificationCode = ?", new Object[]{ true, verificationCode },
                new int[]{ Types.BOOLEAN, Types.INTEGER });

        jdbcTemplate.update("UPDATE users SET verificationCode = ? WHERE verificationCode = ?", new Object[]{ null, verificationCode },
                new int[]{ Types.NULL, Types.INTEGER });
    }

    @Override
    public void changePasswordSolicited(String email, int verificationCode) {
        jdbcTemplate.update("UPDATE users SET verificationCode = ? WHERE mail = ?", new Object[]{ verificationCode, email },
                new int[]{ Types.INTEGER, Types.VARCHAR });

    }

    @Override
    public void changePassword(int verificationCode, String newPassword) {
        jdbcTemplate.update("UPDATE users SET password = ? WHERE verificationCode = ?", new Object[]{ newPassword, verificationCode },
                new int[]{ Types.VARCHAR, Types.INTEGER });
    }

    @Override
    public User getUserByPubId(long pubId) {
        return jdbcTemplate.query("SELECT * FROM users u JOIN publication p ON u.userId = p.userId WHERE p.publicationId = ?", new Object[]{ pubId },
                new int[]{Types.BIGINT}, ROW_MAPPER_USER).stream().findFirst().get();
    }
}
