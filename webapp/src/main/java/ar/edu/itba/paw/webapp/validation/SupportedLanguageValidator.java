package ar.edu.itba.paw.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class SupportedLanguageValidator implements ConstraintValidator<SupportedLanguage, String> {

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("en", "es");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return SUPPORTED_LANGUAGES.contains(value);
    }
}
