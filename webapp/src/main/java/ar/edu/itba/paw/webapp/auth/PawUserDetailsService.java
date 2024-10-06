package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.PublicationService;
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

    private final UserService us;
    private final PublicationService ps;

    public PawUserDetailsService(UserService us, PublicationService ps) {
        this.us = us;
        this.ps = ps;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = us.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException("No such user"));
        Collection<SimpleGrantedAuthority> authorities;

        if(ps.getPublicationCountByUserId(user.getUserId()) > 0) {
            authorities = Set.of(new SimpleGrantedAuthority("PUBLISHER"));
        } else {
            authorities = Set.of(new SimpleGrantedAuthority("EXPLORER"));
        }
        return new PawUserDetails(user, authorities);
    }
}
