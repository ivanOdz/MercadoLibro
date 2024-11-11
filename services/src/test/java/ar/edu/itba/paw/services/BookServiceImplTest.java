package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.BookNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.BookDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookImage;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {
	
	@Mock
	private BookDao bookDao;
	@Mock
    private BookModelService bookModelService;
	@Mock
    private ImageService imageService;
	
	@InjectMocks
	private BookServiceImpl bookServiceImpl;
	
	private static final Long USER_ID = 1L;
	private static final Long BOOK_ID = 1L;
	private static final Long BOOK_MODEL_ID = 1L;
	private static final String BOOK_STATE = "GOOD";
	private static final int RATING = 5;
	private static final int BOOK_COVER_INDEX = 0;
	private static final boolean NEW_BOOK = true;
	private static final int IMAGE_ORDER_1 = 0;
	private static final int IMAGE_ORDER_2 = 1;
	@Mock
	private MultipartFile imageFile1;
	
	@Mock
	private MultipartFile imageFile2;
	
	@Mock
	private User user;

    @Test
    public void testCreateBookUsingProvidedImageList() {
    	
    	List<Image> savedImages;
    	List<BookImage> bookImages;
    	
        Image image1 = mock(Image.class);
        Image image2 = mock(Image.class);
        savedImages = Arrays.asList(image1, image2);
        
        BookImage bookImage_1 = mock(BookImage.class);
        BookImage bookImage_2 = mock(BookImage.class);
        bookImage_1.setImageOrder(IMAGE_ORDER_1);
        bookImage_2.setImageOrder(IMAGE_ORDER_2);
        bookImages = Arrays.asList(bookImage_1, bookImage_2);
        
        User user = spy(User.class);
        Book book = spy(Book.class);
        BookModel book_model = spy(BookModel.class);
        
        user.setUserId(USER_ID);
        book.setBookId(BOOK_ID);
        book.setOwner(user);
        book_model.setBookModelId(BOOK_MODEL_ID);
        book.setImages(new ArrayList<BookImage>());
        
        when(bookModelService.getBookModelByBookModelId(BOOK_MODEL_ID)).thenReturn(book_model);
        when(bookDao.createBook(book_model, user, BookState.valueOf(BOOK_STATE))).thenReturn(book);

        Book createdBook = bookServiceImpl.createBook(BOOK_MODEL_ID, BookState.valueOf(BOOK_STATE), RATING, new ArrayList<MultipartFile>(), BOOK_COVER_INDEX, savedImages, user, NEW_BOOK);
        
        Assert.assertEquals(bookImages.size(), createdBook.getImages().size());
        Assert.assertEquals(bookImages.get(0).getImageOrder(), createdBook.getImages().get(0).getImageOrder());
    }

	@Test(expected = BookNotFoundException.class)
	public void testGetBookByIdNotFoundException() {
		
		bookServiceImpl.getBookById(BOOK_ID);
	}
	
	@Test
	public void testUpdateMissingBookState() {
		
		Book book = bookServiceImpl.updateBookState(BOOK_ID, BOOK_STATE);
		Assert.assertNull(book);
	}
	
}
