package ar.edu.itba.paw.interfaces.services;


import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Optional<User> findById(long id);

    User createUser(String username, String mail);

    Optional<User> findByUsername(String username);
}
