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
import java.util.HashMap;
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
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("exchangeid")
                .withTableName("exchanges");
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
        return jdbcTemplate.query("SELECT * FROM exchanges WHERE exchangeId = ?", new Object[]{ acceptCode },
                new int[]{ Types.BIGINT }, ROWMAPPER).stream().findFirst().get().getId();
    }

    @Override
    public ResponseState exchange(long acceptCode, boolean state) {
        Optional<Exchange> ex = jdbcTemplate.query("SELECT * FROM exchanges WHERE acceptCode = ?", new Object[]{ acceptCode },
                new int[]{ Types.BIGINT }, ROWMAPPER).stream().findFirst();

        if(ex.isEmpty()) {
            // TODO: mandar a una pagina que diga accept code invalido
            return ResponseState.INVALID;
        }
        if(ex.get().getState() == ExchangeState.REJECTED.getValue()){
            return ResponseState.REJECTED;
        }
        if(ex.get().getState() == ExchangeState.ACCEPTED.getValue()){
            return ResponseState.ACCEPTED;
        }
        if(!state) {
            jdbcTemplate.update("UPDATE exchanges SET exchangeState = ? WHERE acceptCode = ?", ExchangeState.REJECTED.getValue(), acceptCode);
            return ResponseState.REJECTED;
        }

        long pubId1 = ex.get().getOfferer();
        long pubId2 = ex.get().getRequester();
        long b1 = publicationsJdbcDao.getPublicationById(pubId1).get().getBookId();
        long b2 = publicationsJdbcDao.getPublicationById(pubId2).get().getBookId();

        bookJdbcDao.exchangeOwnership(b1, b2);

        jdbcTemplate.update("UPDATE exchanges SET exchangeState = ? WHERE acceptCode = ?", ExchangeState.ACCEPTED.getValue(), acceptCode);
        jdbcTemplate.update("UPDATE exchanges SET exchangeState = ? WHERE offerer = ? AND acceptCode <> ?", ExchangeState.REJECTED.getValue(), ex.get().getOfferer(), acceptCode);
        publicationsJdbcDao.terminatePublication(ex.get().getOfferer());
        publicationsJdbcDao.terminatePublication(ex.get().getRequester());

        return ResponseState.ACCEPTED;

    }

    @Override
    public Exchange createExchange(long offererId, long requesterId, long acceptCode) {
        final Map<String, Object> exchangeData = new HashMap<>();
        exchangeData.put("offerer", offererId);
        exchangeData.put("requester", requesterId);
        exchangeData.put("acceptCode", acceptCode);
        exchangeData.put("exchangeState", ExchangeState.PENDING.getValue());

        final Number generatedId = jdbcInsert.executeAndReturnKey(exchangeData);
        return new Exchange(generatedId.longValue(),  offererId,  requesterId,  ExchangeState.PENDING.getValue(), acceptCode);
    }


}
