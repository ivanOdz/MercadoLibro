package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.services.LanguageService;
import ar.edu.itba.paw.models.utils.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getLanguageDisplayName(Language language) {
        Locale locale = LocaleContextHolder.getLocale();
        String key = "language." + language.name().toLowerCase().replace("_", ".");
        return messageSource.getMessage(key, null, locale);
    }
}
