package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.BookImage;
import ar.edu.itba.paw.models.Image;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface BookImageService {
    List<BookImage> getImageByBookId(long bookId);

    List<BookImage> getSortedImagesByBookId(long bookId);

    void saveBookImage(long bookId, Image image, Timestamp timestamp);

}
