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
    
	String baseQuery = "SELECT userReview.userReviewId AS userReviewId, userReview.reviewDescription AS userReviewDescription, userReview.reviewDate AS userReviewDate, userReview.userReviewRating AS userReviewRating\r\n"
						+ ", reviewer.userId AS revieweruserid, reviewer.userName AS reviewerUserName, reviewer.mail AS reviewerMail, reviewer.password AS reviewerPassword, reviewer.imageId AS reviewerImageId, reviewer.verificationCode AS reviewerVerificationCode, reviewer.isVerified AS reviewerIsVerified, reviewer.language AS reviewerLanguage\r\n"
						+ ", subject.userId AS subjectuserid, subject.userName AS subjectUserName, subject.mail AS subjectMail, subject.password AS subjectPassword, subject.imageId AS subjectImageId, subject.verificationCode AS subjectVerificationCode, subject.isVerified AS subjectIsVerified, subject.language AS subjectLanguage\r\n"
						+ ", exchange.exchangeId AS exchangeId, exchange.offererPubId AS exchangeOffererPubId, exchange.exchangeState AS exchangeState, exchange.acceptCode AS exchangeAcceptCode, exchange.offererReceivedBook AS exchangeOffererReceivedBook, exchange.requesterReceivedBook AS exchangeRequesterReceivedBook\r\n"
						+ ", publication.publicationId AS publicationId, publication.publicationState AS publicationState, publication.publicationDatetime AS publicationDateTime\r\n"
						+ ", bookModel.bookModelId AS bookModelId, bookModel.isbn AS bookModelIsbn, bookModel.title AS bookModelTitle, bookModel.editorial AS bookModelEditorial, bookModel.description AS bookModelDescription, bookModel.genre AS bookModelGenre, bookModel.edition AS bookModelEdition, bookModel.weight AS bookModelWeight, bookModel.pages AS bookModelPages, bookModel.bookLanguage AS bookModelBookLanguage, bookModel.dimension AS bookModelDimension, bookModel.publicationYear AS bookModelPublicationYear, bookModel.isPocketEdition AS bookModelIsPocketEdition, bookModel.isHardCover AS bookModelIsHardCover, bookModel.imageId AS bookModelImageId\r\n"
						+ ", book.bookId AS bookId, book.bookState AS bookState, book.exchangesQty AS bookExchangesQty\r\n"
						+ " FROM user_review AS userReview\r\n"
						+ " JOIN users AS reviewer ON userReview.reviewerId = reviewer.userId\r\n"
						+ " JOIN users AS subject ON userReview.subjectId = subject.userId\r\n"
						+ " JOIN exchange AS exchange ON userReview.exchangeId = exchange.exchangeId\r\n"
						+ " JOIN publication AS publication ON userReview.subjectId = publication.userId AND (exchange.offererPubId = publication.publicationId OR exchange.requesterPubId = publication.publicationId)\r\n"
						+ " JOIN book AS book ON publication.bookId = book.bookId\r\n"
						+ " JOIN book_model AS bookModel ON book.bookModelId = bookModel.bookModelId\r\n"
						+ " JOIN users AS actualOwner ON book.ownerId = actualOwner.userId";
    
	private static final RowMapper<UserReview> ROW_MAPPER_USER_REVIEW =
			
		(rs, rowNum) -> {
			
			User reviewer = new User (	rs.getLong("reviewerUserId"),
										rs.getString("reviewerUserName"),
										rs.getString("reviewerMail"),
										rs.getString("reviewerPassword"),
										rs.getLong("reviewerImageId"),
										rs.getInt("reviewerVerificationCode"),
										rs.getBoolean("reviewerIsVerified"),
										rs.getString("reviewerLanguage")
								 	);
			
			User subject = new User (	rs.getLong("subjectUserId"),
										rs.getString("subjectUserName"),
										rs.getString("subjectMail"),
										rs.getString("subjectPassword"),
										rs.getLong("subjectImageId"),
										rs.getInt("subjectVerificationCode"),
										rs.getBoolean("subjectIsVerified"),
										rs.getString("subjectLanguage")
									);
			
			Rating rating = null;
			String authors = null;
			
			BookModel bookModel = new BookModel(	rs.getLong("bookModelId"),
													rs.getString("bookModelIsbn"),
													rs.getString("bookModelTitle"),
													rs.getString("bookModelEditorial"),
													rs.getString("bookModelDescription"),
													Genre.fromInt(rs.getInt("bookModelGenre")),
													rs.getInt("bookModelEdition"),
													rs.getInt("bookModelWeight"),
													rs.getInt("bookModelPages"),
													Language.fromInt(rs.getString("bookModelBookLanguage").equals("es-AR") ? Language.SPANISH.getValue() : Language.ENGLISH.getValue()),
													rs.getInt("bookModelDimension"),
													rs.getShort("bookModelPublicationYear"),
													rs.getBoolean("bookModelIsPocketEdition"),
													rs.getBoolean("bookModelIsHardCover"),
													authors,
													rs.getLong("bookModelImageId"),
													rating
												);
			
			Book bookEarned = new Book(	rs.getLong("bookId"),
										reviewer,
										bookModel,
										BookState.fromInt(rs.getInt("bookState")),
										rs.getInt("bookExchangesQty"),
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
			
			if (rs.getLong("exchangeOffererPubId") == rs.getLong("publicationId")) {
			
				requesterPublication = publicationSelected;
			}
			else {

				offererPublication = publicationSelected;
			}
			
			Exchange exchange = new Exchange(	rs.getLong("exchangeId"),
												offererPublication,
												requesterPublication,
												ExchangeState.fromInt(rs.getInt("exchangeState")),
												rs.getLong("exchangeAcceptCode"),
												rs.getBoolean("exchangeOffererReceivedBook"),
												rs.getBoolean("exchangeRequesterReceivedBook")
											);
			
			UserReview userReview = new UserReview(	rs.getLong("userReviewId"),
													reviewer,
													subject,
													exchange,
													rs.getString("userReviewDescription"),
													rs.getTimestamp("userReviewDate"),
													rs.getInt("userReviewRating")
												);
			
			return userReview;
		};
		
	public UserReviewJdbcDao(final DataSource ds) {
		
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Override
	public List<UserReview> getReviewsGivenByUserId(long userId) {
		
		StringBuilder sqlQuery = new StringBuilder(baseQuery);
		
		sqlQuery.append(" WHERE reviewer.userId = ?");
		
		return jdbcTemplate.query(sqlQuery.toString(), new Object[]{ userId }, new int[]{ Types.BIGINT }, ROW_MAPPER_USER_REVIEW);
	}
	
	@Override
	public List<UserReview> getReviewsEarnedByUserId(long userId) {
		
		StringBuilder sqlQuery = new StringBuilder(baseQuery);
		
		sqlQuery.append(" WHERE subject.userId = ?");
		
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
