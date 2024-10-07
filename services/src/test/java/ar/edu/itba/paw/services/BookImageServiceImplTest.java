package ar.edu.itba.paw.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ar.edu.itba.paw.models.BookImage;

@RunWith(MockitoJUnitRunner.class)
public class BookImageServiceImplTest {
	/*
	@Mock
	private BookImageDao bookImageDao;
	@InjectMocks
	private BookImageServiceImpl bookImageService;
	
	private static final int BOOK_IMAGES = 3;
	private static final long BOOK_ID = 1;
	private static final long IMAGE_ID_1 = 100;
	private static final long IMAGE_ID_2 = 200;
	private static final long IMAGE_ID_3 = 300;
	private static final Timestamp IMAGE_DATETIME = new Timestamp(System.currentTimeMillis());
	
	private BookImage mockBookImage1;
	private BookImage mockBookImage2;
	private BookImage mockBookImage3;
	
	@Before
	public void setUp() {
		mockBookImage1 = new BookImage(BOOK_ID, 1, IMAGE_ID_1, IMAGE_DATETIME);
		mockBookImage2 = new BookImage(BOOK_ID, 2, IMAGE_ID_2, IMAGE_DATETIME);
		mockBookImage3 = new BookImage(BOOK_ID, 3, IMAGE_ID_3, IMAGE_DATETIME);
	}
	
	@Test
	public void testGetImageByBookId() {
		
		when (bookImageDao.getImageByBookId(BOOK_ID)).thenReturn(Arrays.asList(mockBookImage1, mockBookImage2, mockBookImage3));
		
		List<BookImage> images = bookImageService.getImageByBookId(BOOK_ID);
		
		assertNotNull(images);
		assertEquals(BOOK_IMAGES, images.size());
		assertEquals(mockBookImage1, images.get(0));
		assertEquals(mockBookImage2, images.get(1));
		assertEquals(mockBookImage3, images.get(2));
	}
	
	@Test
	public void testGetImageByBookIdNoImages() {
		
		when (bookImageDao.getImageByBookId(BOOK_ID)).thenReturn(Collections.emptyList());
		
		List<BookImage> images = bookImageService.getImageByBookId(BOOK_ID);
		
		assertNotNull(images);
		assertTrue(images.isEmpty());
	}
	
	@Test
	public void testGetSortedImagesByBookId() {
		
		when (bookImageDao.getImageByBookId(BOOK_ID)).thenReturn(Arrays.asList(mockBookImage2, mockBookImage3, mockBookImage1));
		
		List<BookImage> sortedImages = bookImageService.getSortedImagesByBookId(BOOK_ID);
		
		assertNotNull(sortedImages);
		assertEquals(BOOK_IMAGES, sortedImages.size());
		assertEquals(mockBookImage1, sortedImages.get(0));
		assertEquals(mockBookImage2, sortedImages.get(1));
	}
	
	*/
}
