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

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ExchangeJdbcDao implements ExchangeDao {

    private final JdbcTemplate jdbcTemplate;
    private final PublicationJdbcDao publicationJdbcDao;
    private final BookJdbcDao bookJdbcDao;
    private final SimpleJdbcInsert jdbcInsert;


    private static final RowMapper<Exchange> ROWMAPPER =
            (rs, rowNum) -> new Exchange(rs.getLong("exchangeId"), rs.getLong("offererPubId"), rs.getLong("requesterPubId"), ExchangeState.fromInt(rs.getInt("exchangeState")), rs.getInt("acceptCode"), rs.getBoolean("offererReceivedBook"), rs.getBoolean("requesterReceivedBook"), rs.getTimestamp("exchangeDate"));


    public ExchangeJdbcDao(final DataSource ds, PublicationJdbcDao publicationJdbcDao, BookJdbcDao bookJdbcDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        this.publicationJdbcDao = publicationJdbcDao;
        this.bookJdbcDao = bookJdbcDao;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("exchangeid")
                .withTableName("exchange");
    }

    @Override
    public void updateExchangeStatus(int acceptCode, int newStatus) {
        String sql = "UPDATE exchange SET exchangeState = ? WHERE acceptCode = ?";
        jdbcTemplate.update(sql, newStatus, acceptCode);
    }

    @Override
    public Optional<Exchange> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM exchange WHERE exchangeId = ?", new Object[]{ id },
                new int[]{ Types.BIGINT }, ROWMAPPER).stream().findFirst();
    }

    @Override
    public long getIdByAcceptCode(int acceptCode) {
        System.out.println(acceptCode);
        return jdbcTemplate.query("SELECT * FROM exchange WHERE acceptCode = ?", new Object[]{ acceptCode },
                new int[]{ Types.INTEGER }, ROWMAPPER).stream().findFirst().get().getExchangeId();
    }

    @Override
    public ResponseState exchange(int acceptCode, boolean state) {
        Optional<Exchange> ex = jdbcTemplate.query("SELECT * FROM exchange WHERE acceptCode = ?", new Object[]{ acceptCode },
                new int[]{ Types.INTEGER }, ROWMAPPER).stream().findFirst();

        if(ex.isEmpty()) {
            // TODO: mandar a una pagina que diga accept code invalido
            return ResponseState.INVALID;
        }
        if(ex.get().getExchangeState().getValue() == ExchangeState.REJECTED.getValue()){
            return ResponseState.REJECTED;
        }
        if(ex.get().getExchangeState().getValue() == ExchangeState.ACCEPTED.getValue()){
            return ResponseState.ACCEPTED;
        }
        if(!state) {
            jdbcTemplate.update("UPDATE exchange SET exchangeState = ? WHERE acceptCode = ?", ExchangeState.REJECTED.getValue(), acceptCode);
            return ResponseState.REJECTED;
        }

        long pubId1 = ex.get().getOffererPubId();
        long pubId2 = ex.get().getRequesterPubId();
        long b1 = publicationJdbcDao.getPublicationById(pubId1).get().getBookId();
        long b2 = publicationJdbcDao.getPublicationById(pubId2).get().getBookId();

        bookJdbcDao.exchangeOwnership(b1, b2);

        jdbcTemplate.update("UPDATE exchange SET exchangeState = ? WHERE acceptCode = ?", ExchangeState.ACCEPTED.getValue(), acceptCode);
        jdbcTemplate.update("UPDATE exchange SET exchangeState = ? WHERE offererPubId = ? AND acceptCode <> ?", ExchangeState.REJECTED.getValue(), ex.get().getOffererPubId(), acceptCode);
        publicationJdbcDao.terminatePublication(ex.get().getOffererPubId());
        publicationJdbcDao.terminatePublication(ex.get().getRequesterPubId());

        return ResponseState.ACCEPTED;
    }

    @Override
    public Exchange createExchange(long offererPubId, long requesterPubId, int acceptCode) {
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());

        final Map<String, Object> exchangeData = new HashMap<>();
        exchangeData.put("offererPubId", offererPubId);
        exchangeData.put("requesterPubId", requesterPubId);
        exchangeData.put("exchangeState", ExchangeState.PENDING.getValue());
        exchangeData.put("acceptCode", acceptCode);
        exchangeData.put("offererReceivedBook", false);
        exchangeData.put("requesterReceivedBook", false);
        exchangeData.put("exchangeDate", currentTimestamp);

        final Number generatedId = jdbcInsert.executeAndReturnKey(exchangeData);

        return new Exchange(generatedId.longValue(),  offererPubId,  requesterPubId,  ExchangeState.PENDING, acceptCode, false, false, currentTimestamp);
    }
}
