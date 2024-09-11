package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.CompleteBook;

import java.util.List;

public interface CompleteBookService {
    List<CompleteBook> getCompleteAvailableBooksByUserId(long userId);

}
