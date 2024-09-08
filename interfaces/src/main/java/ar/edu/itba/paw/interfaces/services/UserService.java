package ar.edu.itba.paw.interfaces.services;


import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Optional<User> findById(long id);

    Optional<User> findUserByEmail(String mail);

    String findUsernameByEmail(String mail);

    User createUser(String username, String mail, String password);

    Optional<User> findByUsername(String username);
}
