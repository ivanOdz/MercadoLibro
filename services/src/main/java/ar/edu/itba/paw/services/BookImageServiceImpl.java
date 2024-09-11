package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookImageDao;
import ar.edu.itba.paw.interfaces.services.BookImageService;
import ar.edu.itba.paw.models.BookImage;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookImageServiceImpl implements BookImageService {

    private final BookImageDao bookImageDao;

    public BookImageServiceImpl(BookImageDao bookImageDao) {
        this.bookImageDao = bookImageDao;
    }

    @Override
    public List<BookImage> getImageByBookId(long bookId) {
        return bookImageDao.getImageByBookId(bookId);
    }

    @Override
    public List<BookImage> getSortedImagesByBookId(long bookId) {
        return sortImages(getImageByBookId(bookId));
    }

    public List<BookImage> sortImages(List<BookImage> bookImages) {
        return bookImages.stream()
                .sorted(Comparator.comparingInt(BookImage::getImageOrder))
                .collect(Collectors.toList());
    }
}
