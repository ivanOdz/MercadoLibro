package ar.edu.itba.paw.webapp.locale;

import ar.edu.itba.paw.webapp.auth.PawUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class UserLocaleResolver extends SessionLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PawUserDetails pud) {
            String userLanguage = pud.getUser().getLanguage();
            if(userLanguage != null){
                return Locale.forLanguageTag(userLanguage);
            }
        }
        return super.resolveLocale(request);
    }
}