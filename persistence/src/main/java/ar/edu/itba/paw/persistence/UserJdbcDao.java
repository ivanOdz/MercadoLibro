package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Repository
public class UserJdbcDao implements UserDao {

    private static final RowMapper<User> ROWMAPPER =
            (rs, rowNum) -> new User(rs.getLong("userId"),
                                     rs.getString("username"),
                                     rs.getString("mail"),
                                     rs.getString("password"),
                                     rs.getLong("imageId"),
                                     rs.getInt("verificationCode"),
                                     rs.getBoolean("isVerified"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("userid")
                .withTableName("users");
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userId = ?", new Object[]{ id },
                new int[]{ Types.BIGINT }, ROWMAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> find(String mail) {
        return jdbcTemplate.query("SELECT * FROM users WHERE mail = ?", new Object[]{ mail },
        		new int[]{ Types.VARCHAR }, ROWMAPPER).stream().findFirst();
    }
    
    @Override
    public boolean updateUsername(long userId, String newUsername) {
    	int rowsAffected = jdbcTemplate.update("UPDATE users SET username = ? WHERE userId = ?", new Object[]{ newUsername, userId }, new int[]{ Types.VARCHAR, Types.BIGINT });
    	
    	return rowsAffected >= 1;
    }

    @Override
    public User createUser(String username, String mail, String password, int verificationCode) {
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

        final Number userId = jdbcInsert.executeAndReturnKey(userData);

        return new User(userId.longValue(), username, mail, password, null, verificationCode, false);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ? ", new Object[]{ username }, new int[] {Types.VARCHAR}, ROWMAPPER).stream().findFirst();
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
                new int[]{Types.BIGINT}, ROWMAPPER).stream().findFirst().get();
    }
}
