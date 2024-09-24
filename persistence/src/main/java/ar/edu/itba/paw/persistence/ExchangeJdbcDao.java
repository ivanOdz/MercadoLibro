package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ExchangeDao;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.ExchangeWrapper;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.ResponseState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

@Repository
public class ExchangeJdbcDao implements ExchangeDao {

    private final JdbcTemplate jdbcTemplate;
    private final PublicationJdbcDao publicationJdbcDao;
    private final BookJdbcDao bookJdbcDao;
    private final SimpleJdbcInsert jdbcInsert;


    private static final RowMapper<Exchange> ROWMAPPER =
            (rs, rowNum) -> new Exchange(rs.getLong("exchangeId"), rs.getLong("offererPubId"), rs.getLong("requesterPubId"), ExchangeState.fromInt(rs.getInt("exchangeState")), rs.getInt("acceptCode"), rs.getBoolean("offererReceivedBook"), rs.getBoolean("requesterReceivedBook"), rs.getTimestamp("exchangeStartDate"), rs.getTimestamp("exchangeEndDate"));


    private static final RowMapper<ExchangeWrapper> ROWMAPPER_EXCHANGE_WRAPPER =
            (rs, rowNum) -> new ExchangeWrapper(
                    rs.getTimestamp("exchangeStartDate").toLocalDateTime(),
                    rs.getString("acceptCode"),
                    rs.getString("requesterLocation"),
                    rs.getString("requesterMail"),
                    rs.getString("requesterUsername"),
                    rs.getLong("requesterProfileImageId"),
                    rs.getLong("offererProfileImageId"),
                    rs.getString("offererLocation"),
                    rs.getString("offererMail"),
                    rs.getString("offererUsername"),
                    rs.getString("offererBookState"),
                    rs.getString("requesterBookState"),
                    rs.getString("offererBookTitle"),
                    rs.getString("requesterBookTitle"),
                    rs.getLong("reqBookImageId"),
                    rs.getLong("offBookImageId"),
                    rs.getString("requesterBookAuthors"),
                    rs.getString("offererBookAuthors"),
                    rs.getBoolean("offererReceivedBook"),
                    rs.getBoolean("requesterReceivedBook")
            );

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
        //System.out.println(acceptCode);
        return jdbcTemplate.query("SELECT * FROM exchange WHERE acceptCode = ?", new Object[]{ acceptCode },
                new int[]{ Types.INTEGER }, ROWMAPPER).stream().findFirst().get().getExchangeId();
    }

    /*@Override
    public ResponseState exchange(int acceptCode, boolean state) {
        Optional<Exchange> ex = jdbcTemplate.query("SELECT * FROM exchange WHERE acceptCode = ?", new Object[]{ acceptCode },
                new int[]{ Types.INTEGER }, ROWMAPPER).stream().findFirst();

        if(ex.isEmpty()) {
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
    }*/

    @Override
    public Exchange createExchange(long offererPubId, long requesterPubId, int acceptCode, Timestamp startDate) {
        final Map<String, Object> exchangeData = new HashMap<>();
        exchangeData.put("offererPubId", offererPubId);
        exchangeData.put("requesterPubId", requesterPubId);
        exchangeData.put("exchangeState", ExchangeState.PENDING.getValue());
        exchangeData.put("acceptCode", acceptCode);
        exchangeData.put("offererReceivedBook", false);
        exchangeData.put("requesterReceivedBook", false);
        exchangeData.put("exchangeStartDate", startDate);
        exchangeData.put("exchangeEndDate", null);

        Number id = jdbcInsert.executeAndReturnKey(exchangeData);

        return new Exchange(id.longValue(), offererPubId, requesterPubId, ExchangeState.PENDING, acceptCode, false, false, startDate, null);
    }

    /*    private final Exchange exchange;
    exchangeStartDate
    acceptCode
    private final String requesterLocation, requesterMail, requesterUsername;
    private final String offererLocation, offererMail, offererUsername;

    private final Book offererBook, requesterBook; -> offererBookState, requesterBookState, requesterBookRating, offererBookRating,

    private final BookModel offererBookModel, requesterBookModel; -> offererBookTitle, requesterBookTitle
    private final List<BookImage> requesterBookImages, offererBookImages; -> requesterBookImageId, offererBookImageId
    private final List<String> requesterBookAuthor, offererBookAuthor; -> requesterBookAuthors, offererBookAuthors

    rating de la otra persona

    offererReceivedBook, requesterReceivedBook
    offererBookModelEdition
*/
    public List<ExchangeWrapper> getExchangesWhereUserIdIsOfferer(long anUserId, ExchangeState exchangeState){
         //jdbcTemplate.query("SELECT * FROM exchange WHERE offererPubId IN (SELECT publicationId FROM publication WHERE userId = ?) ORDER BY exchangeStartDate DESC", new Object[]{ anUserId },
         //       new int[]{ Types.BIGINT }, ROWMAPPER);

        String sqlQuery = "SELECT e.exchangeStartDate, e.acceptCode, " +
                "reqLocation.locationstring as requesterLocation, requester.mail as requesterMail, requester.username as requesterUsername, " +
                "requesterImage.imageid as requesterProfileImageId, offererImage.imageid as offererProfileImageId, " +
                "offLocation.locationstring as offererLocation, offerer.mail as offererMail, offerer.username as offererUsername, " +
                "bookOfferer.bookstate as offererBookState, bookRequester.bookstate as requesterBookState, " +
                "bmOfferer.title as offererBookTitle, bmRequester.title as requesterBookTitle, " +
                "requesterBookImageId.imageid as reqBookImageId, offererBookImageId.imageid as offBookImageId, " +
                "STRING_AGG(requesterBookAuthor.authorName, ', ') AS requesterBookAuthors, " +
                "STRING_AGG(offererBookAuthor.authorName, ', ') AS offererBookAuthors, " +
                "e.offererReceivedBook, e.requesterReceivedBook " +
                "FROM exchange e " +
                "JOIN publication pubOfferer ON pubOfferer.publicationId = e.offererPubId " +
                "JOIN publication pubRequester ON pubRequester.publicationId = e.requesterPubId " +
                "JOIN book bookOfferer ON bookOfferer.bookId = pubOfferer.bookId " +
                "JOIN book bookRequester ON bookRequester.bookId = pubRequester.bookId " +
                "JOIN users offerer ON pubOfferer.userId = offerer.userId " +
                "JOIN users requester ON pubRequester.userId = requester.userId " +
                "JOIN book_model bmOfferer ON bmOfferer.bookModelId = bookOfferer.bookModelId " +
                "JOIN book_model bmRequester ON bmRequester.bookModelId = bookRequester.bookModelId " +
                "LEFT JOIN book_author baOfferer ON baOfferer.bookModelId = bmOfferer.bookModelId " +
                "LEFT JOIN book_author baRequester ON baRequester.bookModelId = bmRequester.bookModelId " +
                "LEFT JOIN author offererBookAuthor ON offererBookAuthor.authorId = baOfferer.authorId " +
                "LEFT JOIN author requesterBookAuthor ON requesterBookAuthor.authorId = baRequester.authorId " +
                "LEFT JOIN book_image offererBookImage ON offererBookImage.bookId = bookOfferer.bookId " +
                "LEFT JOIN book_image requesterBookImage ON requesterBookImage.bookId = bookRequester.bookId " +
                "LEFT JOIN image offererBookImageId ON offererBookImage.imageId = offererBookImageId.imageId " +
                "LEFT JOIN image requesterBookImageId ON requesterBookImage.imageId = requesterBookImageId.imageId " +
                "LEFT JOIN location reqLocation ON reqLocation.locationId = pubRequester.locationid " +
                "LEFT JOIN location offLocation ON offLocation.locationId = pubOfferer.locationid " +
                "LEFT JOIN image requesterImage ON requesterImage.imageid = requester.imageid " +
                "LEFT JOIN image offererImage ON offererImage.imageid = offerer.imageid " +
                "WHERE e.offererPubId IN (SELECT publicationId FROM publication WHERE userId = ?) " +
                "GROUP BY e.exchangeStartDate, e.acceptCode, reqLocation.locationstring, requester.mail, requester.username, " +
                "requesterImage.imageid, offererImage.imageid, offLocation.locationstring, offerer.mail, offerer.username, " +
                "bookOfferer.bookstate, bookRequester.bookstate, bmOfferer.title, bmRequester.title, " +
                "requesterBookImageId.imageid, offererBookImageId.imageid, e.offererReceivedBook, e.requesterReceivedBook " +
                "ORDER BY e.exchangeStartDate DESC";

        return jdbcTemplate.query(sqlQuery, new Object[]{ anUserId }, new int[]{ Types.BIGINT }, ROWMAPPER_EXCHANGE_WRAPPER);
    }

    public List<Exchange> getExchangesWhereUserIdIsRequester(long anUserId){
        return jdbcTemplate.query("SELECT * FROM exchange WHERE requesterPubId IN (SELECT publicationId FROM publication WHERE userId = ? ) ORDER BY exchangeStartDate DESC", new Object[]{ anUserId },
                new int[]{ Types.BIGINT }, ROWMAPPER);
    }

    @Override
    public void confirmRequester(int acceptCode) {
        jdbcTemplate.update("UPDATE exchange SET requesterReceivedBook = ? WHERE acceptcode = ?", true, acceptCode);

        Optional<Exchange> exchange= jdbcTemplate.query("SELECT * FROM exchange WHERE acceptcode = ?", new Object[]{ acceptCode },
                new int[]{ Types.INTEGER }, ROWMAPPER).stream().findFirst();

        if(exchange.get().isConfirmed())
            updateExchangeStatus(acceptCode, ExchangeState.TERMINATED.getValue());

    }

    @Override
    public void confirmOfferer(int acceptCode) {
        jdbcTemplate.update("UPDATE exchange SET offererReceivedBook = ? WHERE acceptcode = ?", true, acceptCode);

        Optional<Exchange> exchange= jdbcTemplate.query("SELECT * FROM exchange WHERE acceptcode = ?", new Object[]{ acceptCode },
                new int[]{ Types.INTEGER }, ROWMAPPER).stream().findFirst();

        if(exchange.get().isConfirmed())
            updateExchangeStatus(acceptCode, ExchangeState.TERMINATED.getValue());

    }
}
