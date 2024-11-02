package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.PublicationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;


@Component
public class PawUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService us;

    @Autowired
    private PublicationService ps;

    @Autowired
    private MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String errorMessage = messageSource.getMessage("error.userNotFound", new Object[]{s}, LocaleContextHolder.getLocale());
        User user = us.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(errorMessage));
        Collection<SimpleGrantedAuthority> authorities;

        if(ps.getPublicationCountByUserId(user.getUserId()) > 0) {
            authorities = Set.of(new SimpleGrantedAuthority("PUBLISHER"));
        } else {
            authorities = Set.of(new SimpleGrantedAuthority("EXPLORER"));
        }
        return new PawUserDetails(user, authorities);
    }
}
