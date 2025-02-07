package ar.edu.itba.paw.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.regex.Pattern;

public class SupportedLanguageValidator implements ConstraintValidator<SupportedLanguage, String> {

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("en", "es");
    private static final Pattern LOCALE_PATTERN = Pattern.compile("^[a-z]{2}(-[A-Z]{2})?$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return true;

        if (!LOCALE_PATTERN.matcher(value).matches()) return false;
        String baseLanguage = value.split("-")[0];

        return SUPPORTED_LANGUAGES.contains(baseLanguage);
    }
}
