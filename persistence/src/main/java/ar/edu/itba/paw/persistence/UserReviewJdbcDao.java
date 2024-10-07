package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.UserReviewBadRequestException;
import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.Rating;

import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import org.springframework.beans.PropertyBatchUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;


import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

@Repository
public class UserReviewJdbcDao implements UserReviewDao {
	
	private final JdbcTemplate jdbcTemplate;
	
	public UserReviewJdbcDao(final DataSource ds) {
	    jdbcTemplate = new JdbcTemplate(ds);
	}

	@Autowired
	private MessageSource messageSource;
	
	String baseQueryReview = "SELECT userReview.userReviewId AS userReviewId, userReview.reviewDescription AS userReviewDescription, userReview.reviewDate AS userReviewDate, userReview.userReviewRating AS userReviewRating\r\n"
							+ ", exchange.exchangeId AS exchangeId, exchange.exchangeStartDate, exchange.exchangeEndDate, exchange.offererPubId AS exchangeOffererPubId, exchange.exchangeState AS exchangeState, exchange.acceptCode AS exchangeAcceptCode, exchange.offererReceivedBook AS exchangeOffererReceivedBook, exchange.requesterReceivedBook AS exchangeRequesterReceivedBook\r\n"
							// -- Información del lado del que dejó la reseña
							+ ", reviewer.userId AS revieweruserId, reviewer.userName AS reviewerUserName, reviewer.mail AS reviewerMail, reviewer.password AS reviewerPassword, reviewer.imageId AS reviewerImageId, reviewer.verificationCode AS reviewerVerificationCode, reviewer.isVerified AS reviewerIsVerified, reviewer.language AS reviewerLanguage\r\n"
							+ ", publicationReviewer.publicationId AS publicationReviewerId, publicationReviewer.publicationState AS publicationReviewerState, publicationReviewer.publicationDatetime AS publicationReviewerDateTime\r\n"
							+ ", locationReviewer.locationId AS locationReviewerId, locationReviewer.locationString AS locationReviewer\r\n"
							+ ", bookReviewer.bookId AS bookReviewerId, bookReviewer.bookState AS bookReviewerState, bookReviewer.exchangesQty AS bookReviewerExchangesQty\r\n"
							+ ", bookModelReviewer.bookModelId AS bookModelReviewerId, bookModelReviewer.isbn AS bookModelReviewerIsbn, bookModelReviewer.title AS bookModelReviewerTitle, bookModelReviewer.editorial AS bookModelReviewerEditorial, bookModelReviewer.description AS bookModelReviewerDescription, bookModelReviewer.genre AS bookModelReviewerGenre, bookModelReviewer.edition AS bookModelReviewerEdition, bookModelReviewer.weight AS bookModelReviewerWeight, bookModelReviewer.pages AS bookModelReviewerPages, bookModelReviewer.bookLanguage AS bookModelReviewerBookLanguage, bookModelReviewer.dimension AS bookModelReviewerDimension, bookModelReviewer.publicationYear AS bookModelReviewerPublicationYear, bookModelReviewer.isPocketEdition AS bookModelReviewerIsPocketEdition, bookModelReviewer.isHardCover AS bookModelReviewerIsHardCover, bookModelReviewer.imageId AS bookModelReviewerImageId\r\n"
							+ ", ownerBookOfReviewer.userId AS ownerBookOfReviewerId, ownerBookOfReviewer.userName AS ownerBookOfReviewerName, ownerBookOfReviewer.mail AS ownerBookOfReviewerMail, ownerBookOfReviewer.password AS ownerBookOfReviewerPassword, ownerBookOfReviewer.imageId AS ownerBookOfReviewerImageId, ownerBookOfReviewer.verificationCode AS ownerBookOfReviewerVerificationCode, ownerBookOfReviewer.isVerified AS ownerBookOfReviewerIsVerified, ownerBookOfReviewer.language AS ownerBookOfReviewerLanguage\r\n"
							+ ", (SELECT AVG(innerBookRating.rating) AS bookModelRatingReviewer FROM book_rating AS innerBookRating WHERE bookModelReviewer.bookModelId = innerBookRating.bookModelId)\r\n"
							+ ", (SELECT COUNT(innerBookRating.rating) AS bookModelRatingCountReviewer FROM book_rating AS innerBookRating WHERE bookModelReviewer.bookModelId = innerBookRating.bookModelId)\r\n"
							+ ", (SELECT STRING_AGG(authorReviewer.authorName, ', ') FROM book_author AS bookAuthorsReviewer JOIN author AS authorReviewer ON bookAuthorsReviewer.authorId = authorReviewer.authorId WHERE bookModelReviewer.bookModelId = bookAuthorsReviewer.bookModelId) AS bookAuthorsReviewer\r\n"
							+ ", (SELECT ARRAY_AGG(bookImage.imageId ORDER BY bookImagesReviewer.imageOrder) FROM book_image AS bookImagesReviewer JOIN image AS bookImage ON bookImagesReviewer.imageId = bookImage.imageId WHERE bookImagesReviewer.bookId = bookReviewer.bookId) AS bookImagesReviewer\r\n"
							+ ", CASE WHEN NOT EXISTS (SELECT * FROM publication AS publicationReviewerAux LEFT JOIN exchange AS exchangeReviewerAux ON (publicationReviewerAux.publicationId = exchangeReviewerAux.requesterPubId OR publicationReviewerAux.publicationId = exchangeReviewerAux.offererPubId) WHERE bookReviewer.bookId = publicationReviewerAux.bookId AND exchangeReviewerAux.exchangeState = 1) THEN TRUE\r\n"
							+ "		ELSE FALSE\r\n"
							+ "  END AS bookReviewerAvailable\r\n"
							// -- Información del lado del evaluado
							+ ", subject.userId AS subjectuserId, subject.userName AS subjectUserName, subject.mail AS subjectMail, subject.password AS subjectPassword, subject.imageId AS subjectImageId, subject.verificationCode AS subjectVerificationCode, subject.isVerified AS subjectIsVerified, subject.language AS subjectLanguage\r\n"
							+ ", publicationSubject.publicationId AS publicationSubjectId, publicationSubject.publicationState AS publicationSubjectState, publicationSubject.publicationDatetime AS publicationSubjectDateTime\r\n"
							+ ", locationSubject.locationId AS locationSubjectId, locationSubject.locationString AS locationSubject\r\n"
							+ ", bookSubject.bookId AS bookSubjectId, bookSubject.bookState AS bookSubjectState, bookSubject.exchangesQty AS bookSubjectExchangesQty\r\n"
							+ ", bookModelSubject.bookModelId AS bookModelSubjectId, bookModelSubject.isbn AS bookModelSubjectIsbn, bookModelSubject.title AS bookModelSubjectTitle, bookModelSubject.editorial AS bookModelSubjectEditorial, bookModelSubject.description AS bookModelSubjectDescription, bookModelSubject.genre AS bookModelSubjectGenre, bookModelSubject.edition AS bookModelSubjectEdition, bookModelSubject.weight AS bookModelSubjectWeight, bookModelSubject.pages AS bookModelSubjectPages, bookModelSubject.bookLanguage AS bookModelSubjectBookLanguage, bookModelSubject.dimension AS bookModelSubjectDimension, bookModelSubject.publicationYear AS bookModelSubjectPublicationYear, bookModelSubject.isPocketEdition AS bookModelSubjectIsPocketEdition, bookModelSubject.isHardCover AS bookModelSubjectIsHardCover, bookModelSubject.imageId AS bookModelSubjectImageId\r\n"
							+ ", ownerBookOfSubject.userId AS ownerBookOfSubjectId, ownerBookOfSubject.userName AS ownerBookOfSubjectName, ownerBookOfSubject.mail AS ownerBookOfSubjectMail, ownerBookOfSubject.password AS ownerBookOfSubjectPassword, ownerBookOfSubject.imageId AS ownerBookOfSubjectImageId, ownerBookOfSubject.verificationCode AS ownerBookOfSubjectVerificationCode, ownerBookOfSubject.isVerified AS ownerBookOfSubjectIsVerified, ownerBookOfSubject.language AS ownerBookOfSubjectLanguage\r\n"
							+ ", (SELECT AVG(innerBookRating.rating) AS bookModelRatingSubject FROM book_rating AS innerBookRating WHERE bookModelSubject.bookModelId = innerBookRating.bookModelId)\r\n"
							+ ", (SELECT COUNT(innerBookRating.rating) AS bookModelRatingCountSubject FROM book_rating AS innerBookRating WHERE bookModelSubject.bookModelId = innerBookRating.bookModelId)\r\n"
							+ ", (SELECT STRING_AGG(authorSubject.authorName, ', ') FROM book_author AS bookAuthorsSubject JOIN author AS authorSubject ON bookAuthorsSubject.authorId = authorSubject.authorId WHERE bookModelSubject.bookModelId = bookAuthorsSubject.bookModelId) AS bookAuthorsSubject\r\n"
							+ ", (SELECT ARRAY_AGG(bookImage.imageId ORDER BY bookImagesSubject.imageOrder) FROM book_image AS bookImagesSubject JOIN image AS bookImage ON bookImagesSubject.imageId = bookImage.imageId WHERE bookImagesSubject.bookId = bookSubject.bookId) AS bookImagesSubject\r\n"
							+ ", CASE WHEN NOT EXISTS (SELECT * FROM publication AS publicationSubjectAux LEFT JOIN exchange AS exchangeSubjectAux ON (publicationSubjectAux.publicationId = exchangeSubjectAux.requesterPubId OR publicationSubjectAux.publicationId = exchangeSubjectAux.offererPubId) WHERE bookReviewer.bookId = publicationSubjectAux.bookId AND exchangeSubjectAux.exchangeState = 1) THEN TRUE\r\n"
							+ " 	ELSE FALSE\r\n"
							+ "  END AS bookSubjectAvailable\r\n"
							// Joins
							+ " FROM user_review AS userReview\r\n"
							+ " JOIN users AS reviewer ON userReview.reviewerId = reviewer.userId\r\n"
							+ " JOIN users AS subject ON userReview.subjectId = subject.userId\r\n"
							+ " JOIN exchange AS exchange ON userReview.exchangeId = exchange.exchangeId\r\n"
							+ " JOIN publication AS publicationReviewer ON userReview.reviewerId = publicationReviewer.userId\r\n"
							+ " JOIN location AS locationReviewer ON publicationReviewer.locationId = locationReviewer.locationId\r\n"
							+ " JOIN book AS bookReviewer ON publicationReviewer.bookId = bookReviewer.bookId\r\n"
							+ " JOIN book_model AS bookModelReviewer ON bookReviewer.bookModelId = bookModelReviewer.bookModelId\r\n"
							+ " JOIN users AS ownerBookOfReviewer ON bookReviewer.ownerId = ownerBookOfReviewer.userId\r\n"
							+ " JOIN publication AS publicationSubject ON userReview.subjectId = publicationSubject.userId\r\n"
							+ " JOIN location AS locationSubject ON publicationSubject.locationId = locationSubject.locationId\r\n"
							+ " JOIN book AS bookSubject ON publicationSubject.bookId = bookSubject.bookId\r\n"
							+ " JOIN book_model AS bookModelSubject ON bookSubject.bookModelId = bookModelSubject.bookModelId\r\n"
							+ " JOIN users AS ownerBookOfSubject ON bookSubject.ownerId = ownerBookOfSubject.userId\r\n"
							// Filtrar tuplas que no corresponden con los involucrados
							+ " WHERE ((exchange.requesterPubId = publicationReviewer.publicationId AND exchange.offererPubId = publicationSubject.publicationId) OR (exchange.offererPubId = publicationReviewer.publicationId AND exchange.requesterPubId = publicationSubject.publicationId))\r\n";
    
    
	private static final RowMapper<UserReview> ROW_MAPPER_USER_REVIEW =
			
		(rs, rowNum) -> {
			
// ----------------------------------------- REVIWER ------------------------------------------------------------------------------------------------
			
			User reviewer = new User(	rs.getLong("reviewerUserId"),
										rs.getString("reviewerUserName"),
										rs.getString("reviewerMail"),
										rs.getString("reviewerPassword"),
										rs.getLong("reviewerImageId"),
										rs.getInt("reviewerVerificationCode"),
										rs.getBoolean("reviewerIsVerified"),
										rs.getString("reviewerLanguage")
									);
			
			Rating bookModelRatingReviewer = new Rating(rs.getDouble("bookModelRatingReviewer"), rs.getInt("bookModelRatingCountReviewer"));
			Location locationReviewer = new Location(rs.getLong("locationReviewerId"), rs.getString("locationReviewer"));
			List<Integer> bookImagesReviewer = rs.getObject("bookImagesReviewer") == null ? new ArrayList<>() : Arrays.asList((Integer[]) rs.getArray("bookImagesReviewer").getArray());
		
			BookModel bookModelReviewer = new BookModel(	rs.getLong("bookModelReviewerId"),
															rs.getString("bookModelReviewerIsbn"),
															rs.getString("bookModelReviewerTitle"),
															rs.getString("bookModelReviewerEditorial"),
															rs.getString("bookModelReviewerDescription"),
															Genre.fromInt(rs.getInt("bookModelReviewerGenre")),
															rs.getInt("bookModelReviewerEdition"),
															rs.getInt("bookModelReviewerWeight"),
															rs.getInt("bookModelReviewerPages"),
															Language.fromInt(rs.getString("bookModelReviewerBookLanguage").equals("es-AR") ? Language.SPANISH.getValue() : Language.ENGLISH.getValue()),
															rs.getInt("bookModelReviewerDimension"),
															rs.getShort("bookModelReviewerPublicationYear"),
															rs.getBoolean("bookModelReviewerIsPocketEdition"),
															rs.getBoolean("bookModelReviewerIsHardCover"),
															rs.getString("bookAuthorsReviewer"),
															rs.getLong("bookModelReviewerImageId"),
															bookModelRatingReviewer
														);
		
			User ownerBookOfReviewer = new User(	rs.getLong("ownerBookOfReviewerId"),
													rs.getString("ownerBookOfReviewerName"),
													rs.getString("ownerBookOfReviewerMail"),
													rs.getString("ownerBookOfReviewerPassword"),
													rs.getLong("ownerBookOfReviewerImageId"),
													rs.getInt("ownerBookOfReviewerVerificationCode"),
													rs.getBoolean("ownerBookOfReviewerIsVerified"),
													rs.getString("ownerBookOfReviewerLanguage")
												);
			
			Book bookEarnedReviewer = new Book(	rs.getLong("bookReviewerId"),
												ownerBookOfReviewer,
												bookModelReviewer,
												BookState.fromInt(rs.getInt("bookReviewerState")),
												rs.getInt("bookReviewerExchangesQty"),
												rs.getBoolean("bookReviewerAvailable"),
												bookImagesReviewer
											);
			
			Publication publicationReviewer = new Publication(	rs.getLong("publicationReviewerId"),
																bookEarnedReviewer, PublicationState.fromInt(rs.getInt("publicationReviewerState")),
																rs.getTimestamp("publicationReviewerDatetime"),
																locationReviewer
															);

// ----------------------------------------- SUBJECT ------------------------------------------------------------------------------------------------
			
			User subject = new User(	rs.getLong("subjectUserId"),
										rs.getString("subjectUserName"),
										rs.getString("subjectMail"),
										rs.getString("subjectPassword"),
										rs.getLong("subjectImageId"),
										rs.getInt("subjectVerificationCode"),
										rs.getBoolean("subjectIsVerified"),
										rs.getString("subjectLanguage")
									);
			
			Rating bookModelRatingSubject = new Rating(rs.getDouble("bookModelRatingSubject"), rs.getInt("bookModelRatingCountSubject"));
			Location locationSubject = new Location(rs.getLong("locationSubjectId"), rs.getString("locationSubject"));
			List<Integer> bookImagesSubject = rs.getObject("bookImagesSubject") == null ? new ArrayList<>() : Arrays.asList((Integer[]) rs.getArray("bookImagesSubject").getArray());
		
			BookModel bookModelSubject = new BookModel(	rs.getLong("bookModelSubjectId"),
														rs.getString("bookModelSubjectIsbn"),
														rs.getString("bookModelSubjectTitle"),
														rs.getString("bookModelSubjectEditorial"),
														rs.getString("bookModelSubjectDescription"),
														Genre.fromInt(rs.getInt("bookModelSubjectGenre")),
														rs.getInt("bookModelSubjectEdition"),
														rs.getInt("bookModelSubjectWeight"),
														rs.getInt("bookModelSubjectPages"),
														Language.fromInt(rs.getString("bookModelSubjectBookLanguage").equals("es-AR") ? Language.SPANISH.getValue() : Language.ENGLISH.getValue()),
														rs.getInt("bookModelSubjectDimension"),
														rs.getShort("bookModelSubjectPublicationYear"),
														rs.getBoolean("bookModelSubjectIsPocketEdition"),
														rs.getBoolean("bookModelSubjectIsHardCover"),
														rs.getString("bookAuthorsSubject"),
														rs.getLong("bookModelSubjectImageId"),
														bookModelRatingSubject
													);
			
			User ownerBookOfSubject = new User(	rs.getLong("ownerBookOfSubjectId"),
												rs.getString("ownerBookOfSubjectName"),
												rs.getString("ownerBookOfSubjectMail"),
												rs.getString("ownerBookOfSubjectPassword"),
												rs.getLong("ownerBookOfSubjectImageId"),
												rs.getInt("ownerBookOfSubjectVerificationCode"),
												rs.getBoolean("ownerBookOfSubjectIsVerified"),
												rs.getString("ownerBookOfSubjectLanguage")
											);
			
			Book bookEarnedSubject = new Book(	rs.getLong("bookSubjectId"),
												ownerBookOfSubject,
												bookModelSubject,
												BookState.fromInt(rs.getInt("bookSubjectState")),
												rs.getInt("bookSubjectExchangesQty"),
												rs.getBoolean("bookSubjectAvailable"),
												bookImagesSubject
											);
			
			Publication publicationSubject = new Publication(	rs.getLong("publicationSubjectId"),
																bookEarnedSubject, PublicationState.fromInt(rs.getInt("publicationSubjectState")),
																rs.getTimestamp("publicationSubjectDatetime"),
																locationSubject
															);
		
// ----------------------------------------- EXCHANGE AND REVIEW ------------------------------------------------------------------------------------
			
			Publication offererPublication = null;
			Publication requesterPublication = null;
			
			if (rs.getLong("exchangeOffererPubId") == rs.getLong("publicationReviewerId")) {
				
				offererPublication = publicationReviewer;
				requesterPublication = publicationSubject;
				
			} else {
			
				offererPublication = publicationSubject;
				requesterPublication = publicationReviewer;
			}
			
			Exchange exchange = new Exchange(	rs.getLong("exchangeId"),
												offererPublication,
												requesterPublication,
												ExchangeState.fromInt(rs.getInt("exchangeState")),
												rs.getLong("exchangeAcceptCode"),
												rs.getBoolean("exchangeOffererReceivedBook"),
												rs.getBoolean("exchangeRequesterReceivedBook"),
												rs.getTimestamp("exchangeStartDate"),
												rs.getTimestamp("exchangeEndDate")
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
	
	String baseQueryRating = "SELECT COALESCE(AVG(userReviewRating), 5.00) AS averageRating, COUNT(userReviewRating) AS countRating FROM user_review\r\n";
	
	private static final RowMapper<Rating> ROW_MAPPER_RATING =
			
		(rs, rowNum) -> {
			
			return new Rating(rs.getDouble("averageRating"), rs.getInt("countRating"));
		};

    @Override
    public PaginatedResponse<UserReview, BasicMetadata> getReviewsGivenByUserId(long userId) {

        StringBuilder sqlQuery = new StringBuilder(baseQueryReview);

        sqlQuery.append(" AND reviewer.userId = ?");

        List<UserReview> data = jdbcTemplate.query(sqlQuery.toString(), new Object[] { userId }, new int[] { Types.BIGINT }, ROW_MAPPER_USER_REVIEW);

        return new PaginatedResponse<>(data, new BasicMetadata(0, 0, 0));
    }

    @Override
    public PaginatedResponse<UserReview, BasicMetadata> getReviewsEarnedByUserId(long userId) {

        StringBuilder sqlQuery = new StringBuilder(baseQueryReview);

        sqlQuery.append(" AND subject.userId = ?");

        List<UserReview> data = jdbcTemplate.query(sqlQuery.toString(), new Object[] { userId }, new int[] { Types.BIGINT }, ROW_MAPPER_USER_REVIEW);

        return new PaginatedResponse<>(data, new BasicMetadata(0, 0, 0));
    }

    @Override
    public Boolean createUserReview(long exchangeId, long userId, long userSubjectId, String description, int rating) {
		int rowsAffected;
		try {
			String sqlUpdate = "INSERT INTO user_review (exchangeId, reviewerId, subjectId, reviewDescription, reviewDate, userReviewRating) VALUES (?, ?, ?, ?, ?, ?)";

			rowsAffected = jdbcTemplate.update(	sqlUpdate,
					exchangeId,
					userId,
					userSubjectId,
					description,
					new Timestamp(new Date().getTime()),
					rating
			);
		} catch (DataIntegrityViolationException e) {
			throw new UserReviewBadRequestException(messageSource.getMessage("error.createUserReview", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale()));
		}

		return rowsAffected > 0;
    }

    @Override
    public UserReview getUserReviewEarned(long exchangeId, long userId) {
    	
        StringBuilder sqlQuery = new StringBuilder(baseQueryReview);

        sqlQuery.append(" AND exchange.exchangeId = ? AND subject.userId = ? LIMIT 1");
        
        List<UserReview> userReviewEarned = jdbcTemplate.query(sqlQuery.toString(), new Object[] { exchangeId, userId }, new int[] { Types.BIGINT, Types.BIGINT }, ROW_MAPPER_USER_REVIEW);

        return userReviewEarned.isEmpty() ? null : userReviewEarned.getFirst();
    }

    @Override
    public UserReview getUserReviewGiven(long exchangeId, long userId) {

        StringBuilder sqlQuery = new StringBuilder(baseQueryReview);

        sqlQuery.append(" AND exchange.exchangeId = ? AND reviewer.userId = ? LIMIT 1");

        Optional<UserReview> userReviewGiven = jdbcTemplate.query(sqlQuery.toString(), new Object[] { exchangeId, userId }, new int[] { Types.BIGINT, Types.BIGINT }, ROW_MAPPER_USER_REVIEW).stream().findFirst();

		// intended to return null if no element is present
        return userReviewGiven.get();
    }
    
    @Override
    public Rating getUserRatingEarned(long userId) {
    	
    	StringBuilder sqlQuery = new StringBuilder(baseQueryRating);

    	sqlQuery.append(" WHERE subjectId = ?");
    	
    	List<Rating> rating = jdbcTemplate.query(sqlQuery.toString(), new Object[] { userId }, new int[] { Types.BIGINT }, ROW_MAPPER_RATING);
    	
        return rating.isEmpty() ? null : rating.getFirst();
    }

    @Override
    public Rating getUserRatingGiven(long userId) {

    	StringBuilder sqlQuery = new StringBuilder(baseQueryRating);

    	sqlQuery.append(" WHERE ReviewerId = ?");
    	
    	List<Rating> rating = jdbcTemplate.query(sqlQuery.toString(), new Object[] { userId }, new int[] { Types.BIGINT }, ROW_MAPPER_RATING);
    	
        return rating.isEmpty() ? null : rating.getFirst();
    }
}
