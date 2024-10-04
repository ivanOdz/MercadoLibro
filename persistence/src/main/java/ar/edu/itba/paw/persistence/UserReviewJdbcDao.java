package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.interfaces.services.GenreService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Exchange;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserReview;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.Rating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;


import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

@Repository
public class UserReviewJdbcDao implements UserReviewDao {

	private final JdbcTemplate jdbcTemplate;
            
	String baseQuery = "SELECT * FROM user_review AS user_review \r\n"
						+ " JOIN users AS users ON user_review.reviewerId = users.userId \r\n"
						+ " JOIN exchange AS exchange ON user_review.exchangeId = exchange.exchangeId \r\n"
						+ " LEFT JOIN publication AS publication ON user_review.subjectId = publication.userId AND (exchange.offererPubId = publication.publicationId OR exchange.requesterPubId = publication.publicationId) \r\n"
						+ " JOIN book AS book ON publication.bookId = book.bookId \r\n"
						+ " JOIN book_model AS book_model ON book.bookModelId = book_model.bookModelId \r\n";
            
	private static final RowMapper<UserReview> ROW_MAPPER_USER_REVIEW =
			
		(rs, rowNum) -> {
			
			User user = new User (	rs.getLong("userId"),
									rs.getString("userName"),
									rs.getString("mail"),
									rs.getString("password"),
									rs.getLong("imageId"),
									rs.getInt("verificationCode"),
									rs.getBoolean("isVerified"),
									rs.getString("language")
								 );

			Rating rating = null;
			String authors = null;
			
			BookModel bookModel = new BookModel(	rs.getLong("bookModelId"),
													rs.getString("isbn"),
													rs.getString("title"),
													rs.getString("editorial"),
													rs.getString("description"),
													Genre.fromInt(rs.getInt("genre")),
													rs.getInt("edition"),
													rs.getInt("weight"),
													rs.getInt("pages"),
													Language.fromInt(rs.getInt("language")),
													rs.getInt("dimension"),
													rs.getShort("publicationYear"),
													rs.getBoolean("isPocketEdition"),
													rs.getBoolean("isHardCover"),
													authors,
													rs.getLong("imageId"),
													rating
												);
			
			Book bookEarned = new Book(	rs.getLong("bookId"),
										user, bookModel,
										BookState.fromInt(rs.getInt("bookState")),
										rs.getInt("exchangesQty"),
										false,	// isAvailable
										null	// List<images>
									);
			
			Location location = null;
			
			Publication publicationSelected = new Publication(	rs.getLong("publicationId"),
																bookEarned, PublicationState.fromInt(rs.getInt("publicationState")),
																rs.getTimestamp("publicationDatetime"),
																location	
															);	// Esta es la publicación que le interesó y por la cual intercambió el libro, o sea, contraria a la que hizo
			Publication offererPublication = null;
			Publication requesterPublication = null;
			
			if (rs.getLong("offererPubId") == rs.getLong("publicationId")) {
				
				requesterPublication = publicationSelected;
			}
			else {
				
				offererPublication = publicationSelected;
			}
			
			Exchange exchange = new Exchange(	rs.getLong("exchangeId"),
												offererPublication,
												requesterPublication,
												ExchangeState.fromInt(rs.getInt("exchangeState")),
												rs.getLong("acceptCode"),
												rs.getBoolean("offererReceivedBook"),
												rs.getBoolean("requesterReceivedBook")
											);
			
			//(long userReviewId, User reviewer, Exchange exchange, String reviewDescription, Timestamp reviewDate, int reviewRating)
			UserReview userReview = new UserReview(	rs.getLong("userReviewId"),
													user,
													exchange,
													rs.getString("reviewDescription"),
													rs.getTimestamp("reviewDate"),
													rs.getInt("userReviewRating")
												);
			
			return userReview;
		};
		
	public UserReviewJdbcDao(final DataSource ds) {
		
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Override
	public List<UserReview> getReviewsByUserId(long userId) {
		
		StringBuilder sqlQuery = new StringBuilder(baseQuery);
		
		sqlQuery.append(" WHERE reviewerid = ?");
		
		return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId }, new int[]{ Types.BIGINT }, ROW_MAPPER_USER_REVIEW);
	}
	
	@Override
	public Boolean createUserReview(long exchangeId, long userId, String description, int rating) {
		
		return false;
	}
	
	@Override
	public UserReview getUserReview(long exchangeId, long userId) {
		
		return null;
	}
	
	@Override
	public int getUserAverageRatingEarned(long userId) {
		
		return 0;
	}
	
	@Override
	public int getUserAverageRatingGiven(long userId) {
		
		return 0;
	}
	
	@Override
	public boolean isReviewable(long exchangeId, long userId) {
		
		return false;
	}
}
