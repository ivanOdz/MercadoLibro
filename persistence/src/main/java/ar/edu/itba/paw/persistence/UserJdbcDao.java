package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;


@Repository
public class UserJdbcDao implements UserDao {

    private static final RowMapper<User> ROWMAPPER =
            (rs, rowNum) -> new User(rs.getLong("userid"), rs.getString("username"), rs.getString("mail"), rs.getString("password"));

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

    private void updateUsername(User user, String newUsername) {
        jdbcTemplate.update("UPDATE users SET username = ? WHERE userId = ?", new Object[]{ newUsername, user.getId() },
                new int[]{ Types.VARCHAR, Types.BIGINT });
        user.setUsername(newUsername);
    }

    @Override
    public User createUser(String username, String mail, String password) {

        final Map<String, String> userData = Map.of("username", username,"mail", mail, "password", password);
        final Number userId;

        Optional<User> user = find(mail);
        if (user.isPresent()) {
            if (user.get().getMail().compareTo(mail) == 0 && user.get().getUsername().compareTo(username) != 0) {
                updateUsername(user.get(), username);
            }
            return user.get();
        } else {
            userId = jdbcInsert.executeAndReturnKey(userData);
        }

        return new User(userId.longValue(), username, mail, password);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ? ", new Object[]{ username }, new int[] {Types.VARCHAR}, ROWMAPPER).stream().findFirst();
    }
}
