package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.BookModel;
import org.springframework.stereotype.Service;

@Service
public interface BookModelService {

    BookModel getBookModelByBookModelId(long bookModelId);
}
