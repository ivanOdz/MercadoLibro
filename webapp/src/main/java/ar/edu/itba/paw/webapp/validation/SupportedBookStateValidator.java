package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.utils.BookState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class SupportedBookStateValidator implements ConstraintValidator<SupportedBookState, String> {
    private static final Set<String> SUPPORTED_BOOK_STATES = Set.of(
            BookState.NEW.getValue(),
            BookState.LIKE_NEW.getValue(),
            BookState.VERY_GOOD.getValue(),
            BookState.GOOD.getValue(),
            BookState.ACCEPTABLE.getValue(),
            BookState.WORN.getValue());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;
        return SUPPORTED_BOOK_STATES.contains(value.toLowerCase());
    }
}
