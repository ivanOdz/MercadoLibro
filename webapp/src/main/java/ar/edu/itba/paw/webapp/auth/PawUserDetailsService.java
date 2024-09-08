package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class PawUserDetailsService implements UserDetailsService {

    private UserService us;

    public PawUserDetailsService(final UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = us.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("No such user"));
        //TODO: aplicar reglas de negocio para definir los roles
        // intenten qeu los mapeos entre tipos de usuarios y roles no sean 1 a 1, realmente tener roles granulares
        Collection<SimpleGrantedAuthority> authorities = Set.of("ROLE_USER", "ROLE_EDITOR", "ROLE_REVIEWER")
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new PawUserDetails(user, authorities);
    }
}
