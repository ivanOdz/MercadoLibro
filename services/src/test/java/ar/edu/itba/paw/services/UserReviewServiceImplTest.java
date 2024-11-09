package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.ExchangeState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.Rating;
import ar.edu.itba.paw.models.utils.pagination.BasicMetadata;
import ar.edu.itba.paw.interfaces.persistence.UserReviewDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//
//@RunWith(MockitoJUnitRunner.class)
//public class UserReviewServiceImplTest {
//	
//	@Mock
//	private UserReviewDao userReviewDao;
//	
//	@InjectMocks
//	private UserReviewServiceImpl userReviewService;
//	
//	User user1 = new User(1L, "Usuario1", "usuario1@example.com", "encodedPassword", 1L, 1234, true, "es-AR");
//	User user2 = new User(2L, "Usuario2", "usuario2@example.com", "encodedPassword", 2L, 4321, true, "es-AR");
//	User user3 = new User(3L, "Usuario3", "usuario3@example.com", "encodedPassword", 3L, 0123, true, "es-AR");
//	User user4 = new User(4L, "Usuario4", "usuario4@example.com", "encodedPassword", 4L, 3210, true, "es-AR");
//	
//	Genre genre1 = Genre.FICTION;
//	Genre genre2 = Genre.NON_FICTION;
//	Language language = Language.ENGLISH;
//	Rating rating = new Rating(4.5, 8);
//	List<Author> authors = new ArrayList<>();
//
//	BookModel bookModel1 = new BookModel(Long.valueOf(1), "9783161484100", "Título1", "Editorial1", "Descripción1", genre1, 1, 300, 350, language, 20, (short) 2021, false, true, "Autor2", null, rating);
//	BookModel bookModel2 = new BookModel(Long.valueOf(2), "9781617290548", "Título2", "Editorial2", "Descripción2", genre2, 2, 250, 250, language, 15, (short) 2020, true, false, "Autor2", null, rating);
//	BookModel bookModel3 = new BookModel(Long.valueOf(3), "9780123748570", "Título3", "Editorial3", "Descripción3", genre1, 1, 400, 400, language, 22, (short) 2019, false, true, "Autor3", null, rating);
//	BookModel bookModel4 = new BookModel(Long.valueOf(4), "9780070428539", "Título4", "Editorial4", "Descripción4", genre2, 3, 350, 300, language, 25, (short) 2022, true, true, "Autor4", null, rating);
//	
//	Book book1 = new Book(1, user1, bookModel1, BookState.NEW, 3, true, List.of(1, 2));
//	Book book2 = new Book(2, user2, bookModel2, BookState.LIKE_NEW, 5, true, List.of(3, 4));
//	Book book3 = new Book(3, user3, bookModel3, BookState.VERY_GOOD, 2, false, List.of(5, 6));
//	Book book4 = new Book(4, user4, bookModel4, BookState.GOOD, 4, true, List.of(7, 8));
//	
//	Location location1 = new Location(Long.valueOf(1), "Buenos Aires, Argentina");
//	Location location2 = new Location(Long.valueOf(2), "Córdoba, Argentina");
//	Location location3 = new Location(Long.valueOf(3), "Mendoza, Argentina");
//	Location location4 = new Location(Long.valueOf(4), "Rosario, Argentina");
//	
//    @Test
//    public void testGetReviewsByUserId() {
//
//        long exchangeId = 100;
//        int userReviewRating = 5;
//        int currentPage = 1;
//        int pageSize = 1;
//        
//        Timestamp reviewDate = new Timestamp(System.currentTimeMillis());
//        String reviewDescription = "Buen libro :)";
//
//        Publication offererPub = new Publication(1, book1, PublicationState.CURRENT, reviewDate, location1);
//        Publication requesterPub = new Publication(2, book2, PublicationState.CURRENT, reviewDate, location2);
//        Exchange exchange = new Exchange(exchangeId, offererPub, requesterPub, ExchangeState.ACCEPTED, 123456, true, true, reviewDate, reviewDate);
//
//        UserReview mockReview1 = new UserReview(1L, user1, user2, exchange, reviewDescription, reviewDate, userReviewRating);
//        
//        List<UserReview> mockReviews = Arrays.asList(mockReview1);
//        BasicMetadata metadata = new BasicMetadata(currentPage, mockReviews.size(), pageSize);
//        PaginatedResponse<UserReview, BasicMetadata> mockResponse = new PaginatedResponse<>(mockReviews, metadata);
//
//        when(userReviewDao.getReviewsGivenByUserId(user1.getUserId(), 1)).thenReturn(mockResponse);
//
//        PaginatedResponse<UserReview, BasicMetadata> response  = userReviewService.getReviewsGivenByUserId(user1.getUserId(), currentPage);
//
//        assertNotNull(response);
//        assertEquals(mockReviews.size(), response.getMetadata().getTotalResults());
//        assertEquals(currentPage, response.getMetadata().getCurrentPage());
//    }
//}
