package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class SupportedGenreValidator implements ConstraintValidator<SupportedGenre, String> {
    private static final Set<String> SUPPORTED_GENRES = Set.of(
            Genre.ADVENTURE.getValue(),
            Genre.FANTASY.getValue(),
            Genre.HORROR.getValue(),
            Genre.MYSTERY.getValue(),
            Genre.SCIENCE_FICTION.getValue(),
            Genre.ROMANCE.getValue(),
            Genre.THRILLER.getValue(),
            Genre.WESTERN.getValue(),
            Genre.CRIME.getValue(),
            Genre.DYSTOPIAN.getValue(),
            Genre.LITERARY_FICTION.getValue(),
            Genre.HISTORICAL_FICTION.getValue(),
            Genre.HISTORICAL_NON_FICTION.getValue(),
            Genre.BIOGRAPHY.getValue(),
            Genre.AUTOBIOGRAPHY.getValue(),
            Genre.MEMOIR.getValue(),
            Genre.YOUNG_ADULT.getValue(),
            Genre.CHILDRENS_LITERATURE.getValue(),
            Genre.GRAPHIC_NOVEL.getValue(),
            Genre.CLASSIC.getValue(),
            Genre.FICTION.getValue(),
            Genre.NON_FICTION.getValue(),
            Genre.SELF_HELP.getValue(),
            Genre.POETRY.getValue(),
            Genre.CONTEMPORARY.getValue(),
            Genre.RELIGIOUS_SPIRITUAL.getValue(),
            Genre.PHILOSOPHY.getValue(),
            Genre.SCIENCE.getValue(),
            Genre.TRAVEL.getValue(),
            Genre.TRUE_CRIME.getValue(),
            Genre.OTHER.getValue());
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;
        return SUPPORTED_GENRES.contains(value.toLowerCase());
    }
}
