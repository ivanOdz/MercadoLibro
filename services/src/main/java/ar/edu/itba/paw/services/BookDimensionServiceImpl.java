package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.BookDimensionService;
import ar.edu.itba.paw.models.utils.BookDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class BookDimensionServiceImpl implements BookDimensionService {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getDimensionDisplayName(BookDimension bookDimension) {
        Locale locale = LocaleContextHolder.getLocale();
        String key = "dimension." + bookDimension.name().toLowerCase().replace("_", ".");
        return messageSource.getMessage(key, null, locale);
    }
}
