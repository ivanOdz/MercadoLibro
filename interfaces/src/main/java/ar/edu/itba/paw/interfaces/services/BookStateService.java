package ar.edu.itba.paw.interfaces.services;

import org.springframework.stereotype.Service;

import ar.edu.itba.paw.models.utils.BookState;

@Service
public interface BookStateService {
	
	String getBookStateDisplayName(BookState bookState);
}