package ar.edu.itba.paw.services;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.services.BookStateService;
import ar.edu.itba.paw.models.utils.BookState;

@Service
public class BookStateServiceImpl implements BookStateService {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public String getBookStateDisplayName(BookState bookState) {
		
		Locale locale = LocaleContextHolder.getLocale();
		String key = "bookState." + bookState.name().toLowerCase().replace("_", ".");
		return messageSource.getMessage(key, null, locale);
	}
}