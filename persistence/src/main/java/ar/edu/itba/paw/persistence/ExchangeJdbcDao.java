package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.Exchange;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Optional;

@Repository
public class ExchangeJdbcDao implements ExchangeDao {

    private final JdbcTemplate jdbcTemplate;

    public ExchangeJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public void updateExchangeStatus(long acceptCode, int newStatus) {
        String sql = "UPDATE exchanges SET exchangestate = ? WHERE acceptCode = ?";
        jdbcTemplate.update(sql, newStatus, acceptCode);
    }

    @Override
    public Exchange findById(long exchangeId) {
        return null;
    }
}
