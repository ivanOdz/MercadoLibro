package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;

public interface LanguageService {
    String getLanguageDisplayName(Language genre);
}
