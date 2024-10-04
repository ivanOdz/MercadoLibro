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
    
	String baseQuery = "SELECT userReview.userReviewId, userReview.reviewDescription, userReview.reviewDate, userReview.userReviewRating"
						+ ", reviewer.userId AS reviewerUserId, reviewer.userName, reviewer.mail, reviewer.password, reviewer.imageId, reviewer.verificationCode, reviewer.isVerified, reviewer.language"
						+ ", subject.userId AS subjectUserId, subject.userName, subject.mail, subject.password, subject.imageId, subject.verificationCode, subject.isVerified, subject.language"
						+ ", exchange.exchangeId, exchange.exchangeState, exchange.acceptCode, exchange.offererReceivedBook, exchange.requesterReceivedBook"
						+ ", publication.publicationId, publication.publicationState, publication.publicationDatetime"
						+ ", bookModel.bookModelId, bookModel.isbn, bookModel.title, bookModel.editorial, bookModel.description, bookModel.genre, bookModel.edition, bookModel.weight, bookModel.pages, bookModel.bookLanguage, bookModel.dimension, bookModel.publicationYear, bookModel.isPocketEdition, bookModel.isHardCover, bookModel.imageId"
						+ ", book.bookId, book.bookState, book.exchangesQty\r\n"
						+ " FROM user_review AS userReview\r\n"
						+ " JOIN users AS reviewer ON userReview.reviewerId = reviewer.userId\r\n"
						+ " JOIN users AS subject ON userReview.subjectId = subject.userId\r\n"
						+ " JOIN exchange AS exchange ON userReview.exchangeId = exchange.exchangeId\r\n"
						+ " JOIN publication AS publication ON userReview.subjectId = publication.userId AND (exchange.offererPubId = publication.publicationId OR exchange.requesterPubId = publication.publicationId)\r\n"
						+ " JOIN book AS book ON publication.bookId = book.bookId\r\n"
						+ " JOIN book_model AS bookModel ON book.bookModelId = bookModel.bookModelId\r\n"
						+ " JOIN users AS actualOwner ON book.ownerId = actualOwner.userId\r\n";
    
	private static final RowMapper<UserReview> ROW_MAPPER_USER_REVIEW =
			
		(rs, rowNum) -> {
			
			User reviewer = new User (	rs.getLong("reviewer.userId"),
										rs.getString("reviewer.userName"),
										rs.getString("reviewer.mail"),
										rs.getString("reviewer.password"),
										rs.getLong("reviewer.imageId"),
										rs.getInt("reviewer.verificationCode"),
										rs.getBoolean("reviewer.isVerified"),
										rs.getString("reviewer.language")
								 	);
			
			User subject = new User (	rs.getLong("subject.userId"),
										rs.getString("subject.userName"),
										rs.getString("subject.mail"),
										rs.getString("subject.password"),
										rs.getLong("subject.imageId"),
										rs.getInt("subject.verificationCode"),
										rs.getBoolean("subject.isVerified"),
										rs.getString("subject.language")
									);
			
			Rating rating = null;
			String authors = null;
			
			BookModel bookModel = new BookModel(	rs.getLong("bookModel.bookModelId"),
													rs.getString("bookModel.isbn"),
													rs.getString("bookModel.title"),
													rs.getString("bookModel.editorial"),
													rs.getString("bookModel.description"),
													Genre.fromInt(rs.getInt("bookModel.genre")),
													rs.getInt("bookModel.edition"),
													rs.getInt("bookModel.weight"),
													rs.getInt("bookModel.pages"),
													Language.fromInt(rs.getString("bookModel.bookLanguage") == "es-AR" ? Language.SPANISH.getValue() : Language.ENGLISH.getValue()),
													rs.getInt("bookModel.dimension"),
													rs.getShort("bookModel.publicationYear"),
													rs.getBoolean("bookModel.isPocketEdition"),
													rs.getBoolean("bookModel.isHardCover"),
													authors,
													rs.getLong("bookModel.imageId"),
													rating
												);
			
			Book bookEarned = new Book(	rs.getLong("book.bookId"),
										reviewer,
										bookModel,
										BookState.fromInt(rs.getInt("book.bookState")),
										rs.getInt("book.exchangesQty"),
										false,	// isAvailable
										null	// List<images>
									);
			
			Location location = null;
			
			Publication publicationSelected = new Publication(	rs.getLong("publication.publicationId"),
																bookEarned, PublicationState.fromInt(rs.getInt("publication.publicationState")),
																rs.getTimestamp("publication.publicationDatetime"),
																location	
															);	// Esta es la publicación que le interesó y por la cual intercambió el libro, o sea, contraria a la que hizo
			Publication offererPublication = null;
			Publication requesterPublication = null;
			
			if (rs.getLong("exchange.offererPubId") == rs.getLong("publication.publicationId")) {
			
				requesterPublication = publicationSelected;
			}
			else {

				offererPublication = publicationSelected;
			}
			
			Exchange exchange = new Exchange(	rs.getLong("exchange.exchangeId"),
												offererPublication,
												requesterPublication,
												ExchangeState.fromInt(rs.getInt("exchange.exchangeState")),
												rs.getLong("exchange.acceptCode"),
												rs.getBoolean("exchange.offererReceivedBook"),
												rs.getBoolean("exchange.requesterReceivedBook")
											);
			
			UserReview userReview = new UserReview(	rs.getLong("userReview.userReviewId"),
													reviewer,
													subject,
													exchange,
													rs.getString("userReview.reviewDescription"),
													rs.getTimestamp("userReview.reviewDate"),
													rs.getInt("userReview.userReviewRating")
												);
			
			return userReview;
		};
		
	public UserReviewJdbcDao(final DataSource ds) {
		
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Override
	public List<UserReview> getReviewsGivenByUserId(long userId) {
		
		StringBuilder sqlQuery = new StringBuilder(baseQuery);
		
		sqlQuery.append(" WHERE reviewerUserId = ?");
		
		return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId }, new int[]{ Types.BIGINT }, ROW_MAPPER_USER_REVIEW);
	}
	
	@Override
	public List<UserReview> getReviewsEarnedByUserId(long userId) {
		
		StringBuilder sqlQuery = new StringBuilder(baseQuery);
		
		sqlQuery.append(" WHERE subjectUserId = ?");
		
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
