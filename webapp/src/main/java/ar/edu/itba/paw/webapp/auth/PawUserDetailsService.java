package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;


@Component
public class PawUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService us;

    @Autowired
    private PublicationService ps;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = us.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Collection<SimpleGrantedAuthority> authorities;

        if(ps.getPublicationCountByUserId(user.getUserId()) > 0) {
            authorities = Set.of(new SimpleGrantedAuthority("PUBLISHER"));
        } else {
            authorities = Set.of(new SimpleGrantedAuthority("EXPLORER"));
        }

        Locale locale = Locale.forLanguageTag(user.getLanguage());
        LocaleContextHolder.setLocale(locale);
        return new PawUserDetails(user, authorities);
    }
}
