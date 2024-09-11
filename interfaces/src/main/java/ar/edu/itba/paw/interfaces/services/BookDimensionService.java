package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.utils.BookDimension;
import org.springframework.stereotype.Service;


@Service
public interface BookDimensionService {
    String getDimensionDisplayName(BookDimension bookDimension);
}
