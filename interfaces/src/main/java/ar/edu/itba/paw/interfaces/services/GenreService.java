package ar.edu.itba.paw.interfaces.services;

import org.springframework.stereotype.Service;

import ar.edu.itba.paw.models.utils.Genre;

@Service
public interface GenreService {
	
	String getGenreDisplayName(Genre genre);
}