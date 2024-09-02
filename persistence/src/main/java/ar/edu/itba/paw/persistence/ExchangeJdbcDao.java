package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.Exchange;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Optional;

@Repository
public class ExchangeJdbcDao implements ExchangeDao {

    private final JdbcTemplate jdbcTemplate;



    private static final RowMapper<Exchange> ROWMAPPER =
            (rs, rowNum) -> new Exchange(rs.getLong("exchangeId"), rs.getLong("offerer"), rs.getLong("requester"), rs.getInt("exchangeState"), rs.getLong("acceptCode"));


    public ExchangeJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void updateExchangeStatus(long acceptCode, int newStatus) {
        String sql = "UPDATE exchanges SET exchangestate = ? WHERE acceptCode = ?";
        jdbcTemplate.update(sql, newStatus, acceptCode);
    }

    @Override
    public Optional<Exchange> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM exchanges WHERE exchangeId = ?", new Object[]{ id },
                new int[]{ Types.BIGINT }, ROWMAPPER).stream().findFirst();
    }

    @Override
    public long getIdByAcceptCode(long acceptCode) {
            return jdbcTemplate.queryForObject("SELECT exchangeId FROM exchanges WHERE acceptCode = ?", new Object[]{ acceptCode }, Long.class);
    }
}
