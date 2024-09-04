package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    User createUser(String username, String mail);
    
    public Optional<User> find(String username, String mail);
}
