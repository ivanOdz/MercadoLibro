package ar.edu.itba.paw.services;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.services.GenreService;
import ar.edu.itba.paw.models.utils.Genre;

@Service
public class GenreServiceImpl implements GenreService {
	
	@Autowired
	private MessageSource messageSource;
	
	public String getGenreDisplayName(Genre genre) {
		
		Locale locale = new Locale("es");
		String key = "genre." + genre.name().toLowerCase().replace("_", ".");
		
		return messageSource.getMessage(key, null, locale);
	}
}