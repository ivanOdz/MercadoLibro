package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class UserJdbcDao implements UserDao {

    private static final RowMapper<User> ROWMAPPER =
            (rs, rowNum) -> new User(rs.getLong("userid"), rs.getString("username"), rs.getString("mail"));

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
        return jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", new Object[]{ id },
                new int[]{ Types.BIGINT }, ROWMAPPER).stream().findFirst();
    }

    @Override
    public User create(String username, String mail) {
        final Map<String, String> userData = Map.of("username", username, "mail", mail);
        final Number generatedId = jdbcInsert.executeAndReturnKey(userData);
        return new User(generatedId.longValue(), username, mail);
    }
}
