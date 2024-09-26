package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PawUserDetails extends User {

    private static final long serialVersionUID = 2667993229031225137L;

    private final ar.edu.itba.paw.models.User user;

    public PawUserDetails(final ar.edu.itba.paw.models.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), user.isVerified(), true, true, true, authorities);
        this.user = user;
    }

    public ar.edu.itba.paw.models.User getUser() {
        return user;
    }

}
