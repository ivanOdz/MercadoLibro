package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.BookModelDao;
import ar.edu.itba.paw.interfaces.services.BookModelService;
import ar.edu.itba.paw.models.BookModel;
import org.springframework.stereotype.Service;

@Service
public class BookModelServiceImpl implements BookModelService {

    private final BookModelDao bookModelDao;

    public BookModelServiceImpl(BookModelDao bookModelDao) {
        this.bookModelDao = bookModelDao;
    }


    @Override
    public BookModel getBookModelByBookModelId(long bookModelId) {
        return bookModelDao.getBookModelByBookModelId(bookModelId);
    }
}
