package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.BookImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookImageService {
    List<BookImage> getImageByBookId(long bookId);

}
