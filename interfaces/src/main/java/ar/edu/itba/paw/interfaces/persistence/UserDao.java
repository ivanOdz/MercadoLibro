package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    User createUser(String username, String mail, String password);
    
    Optional<User> find(String mail);

    Optional<User> findByUsername(String username);
}
