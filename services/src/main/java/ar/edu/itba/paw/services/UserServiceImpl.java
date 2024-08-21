package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Optional<User> findById(long id) {
        return Optional.of(new User("Paw"));
    }

}
