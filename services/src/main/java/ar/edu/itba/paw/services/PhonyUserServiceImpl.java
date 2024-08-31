package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhonyUserServiceImpl implements UserService {
    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }
}
