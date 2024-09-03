package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.ResponseState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;

@Repository
public class ExchangeJdbcDao implements ExchangeDao {

    private final JdbcTemplate jdbcTemplate;
    private final PublicationsJdbcDao publicationsJdbcDao;
    private final BookJdbcDao bookJdbcDao;
    private final SimpleJdbcInsert jdbcInsert;


    private static final RowMapper<Exchange> ROWMAPPER =
            (rs, rowNum) -> new Exchange(rs.getLong("exchangeId"), rs.getLong("offerer"), rs.getLong("requester"), rs.getInt("exchangeState"), rs.getLong("acceptCode"));


    public ExchangeJdbcDao(final DataSource ds, PublicationsJdbcDao publicationsJdbcDao, BookJdbcDao bookJdbcDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        this.publicationsJdbcDao = publicationsJdbcDao;
        this.bookJdbcDao = bookJdbcDao;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("exchangeId").withTableName("exchanges");
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

    @Override
    public ResponseState exchange(long acceptCode, boolean state) {
        Optional<Exchange> ex = jdbcTemplate.query("SELECT * FROM exchanges WHERE acceptCode = ?", new Object[]{ acceptCode },
                new int[]{ Types.BIGINT }, ROWMAPPER).stream().findFirst();

        if(ex.isEmpty()) {
            // Accept code is invalid, redirect to invalid code view
            // TODO: mandar a una pagina que diga accept code invalido
            return ResponseState.INVALID;
        }
        if(ex.get().getState() == ExchangeState.REJECTED.getValue()){
            // It had been already rejected
            return ResponseState.REJECTED;
        }
        if(ex.get().getState() == ExchangeState.ACCEPTED.getValue()){
            // It had been already acepted
            return ResponseState.ACCEPTED;
        }
        if(!state) {
            // Reject, send rejection email and redirect.
            jdbcTemplate.update("UPDATE exchanges SET exchangeState = ? WHERE acceptCode = ?", ExchangeState.REJECTED.getValue(), acceptCode);
            return ResponseState.REJECTED;
        }

        // In case state = true and a valid accept code:
        // Exchange ownership of books (In BookDao)
        long pubId1 = ex.get().getOfferer();
        long pubId2 = ex.get().getRequester();
        long b1 = publicationsJdbcDao.getPublicationById(pubId1).get().getBookId();
        long b2 = publicationsJdbcDao.getPublicationById(pubId2).get().getBookId();

        bookJdbcDao.exchangeOwnership(b1, b2);

        // Update exchangeState
        jdbcTemplate.update("UPDATE exchanges SET exchangeState = ? WHERE acceptCode = ?", ExchangeState.ACCEPTED.getValue(), acceptCode);
        return ResponseState.ACCEPTED;

        //final Map<String, String> exchangeData = Map.of("offerer", userId1, "requester", userId2,"exchangeState",,"acceptCode",,);
        //final Number gerenatedId = jdbcInsert.executeAndReturnKey(exchangeData);
    }
}
