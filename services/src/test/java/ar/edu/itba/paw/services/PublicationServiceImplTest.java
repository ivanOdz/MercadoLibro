package ar.edu.itba.paw.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookImage;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Publication;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.PublicationState;
import ar.edu.itba.paw.models.utils.Rating;
/*
@RunWith(MockitoJUnitRunner.class)
public class PublicationServiceImplTest {

    @Mock
    private PublicationService publicationService;
    @Mock
    private BookService bookService;
    @Mock
    private BookModelService bookModelService;

    @InjectMocks
    private PublicationServiceImpl publicationDetailService;

    private static final long PUBLICATION_ID = 1;
    private static final long BOOK_ID = 100;
    private static final long BOOK_MODEL_ID = 200;
    private static final long OWNER_ID = 300;
    private static final int EXCHANGES_QTY = 5;
    private static final int RATING = 4;
    private static final long IMAGE_ID = 400;
    private static final int IMAGE_ORDER = 1;
    private static final Timestamp IMAGE_DATETIME = new Timestamp(System.currentTimeMillis());
    
    @Test
    public void testGetPublicationDetail() {
        
        Book mockBook = new Book(BOOK_ID, new User(OWNER_ID, "username", "mail", "password", IMAGE_ID, 1234, true, "EN"), 
                                 new BookModel(BOOK_MODEL_ID, "9874567890123", "Titulo de libro", "Nombre de editorial", 
                                 "Esta es mi descripcion", Genre.FICTION, 1, 500, 300, Language.SPANISH, 
                                 20, (short) 2022, false, true, "Autor 1, Autor 2", IMAGE_ID, null), 
                                 BookState.NEW, EXCHANGES_QTY, true, new ArrayList<>());
        
        Publication mockPublication = new Publication(PUBLICATION_ID, mockBook, PublicationState.CURRENT, IMAGE_DATETIME, null);
        when(publicationService.getPublicationByPublicationId(PUBLICATION_ID)).thenReturn(mockPublication);

        when(bookService.getBookById(BOOK_ID)).thenReturn(mockBook);

        BookModel mockBookModel = mockBook.getBookModel();
        when(bookModelService.getBookModelByBookModelId(BOOK_MODEL_ID)).thenReturn(mockBookModel);
        
        BookImage mockBookImage = new BookImage(BOOK_ID, IMAGE_ORDER, IMAGE_ID, IMAGE_DATETIME);
        List<BookImage> mockBookImageList = new ArrayList<>();
        mockBookImageList.add(mockBookImage);
        
        //when(bookImageService.getImageByBookId(BOOK_ID)).thenReturn(mockBookImageList);

        Rating mockRating = new Rating(4.5, 10);

        when(bookModelService.getBookModelByBookModelId(BOOK_MODEL_ID)).thenReturn(mockBookModel);

      //  Publication publicationDetail = publicationDetailService.getPublicationByPublicationId(PUBLICATION_ID);
      //  assertNotNull(publicationDetail);
        
       // assertEquals(mockBookModel, publicationDetail.getBook());
       // assertEquals(mockBook, publicationDetail.getBook());
//        assertEquals(mockBookImageList, publicationDetail.getImages());
//        assertEquals(mockRating, publicationDetail.getRating());
    }
}*/
