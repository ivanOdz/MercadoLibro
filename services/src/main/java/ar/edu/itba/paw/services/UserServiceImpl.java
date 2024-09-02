package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;


    public UserServiceImpl(final UserDao userDao) {
        this.userDao = userDao;
    }
    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public User create(String username, String mail) {
        //register user
        //TODO
        //  1. validar inputs
        //  2. ingresarlo en la base de datos
        //  3. generar un Token de validacion y guardarlo en la base de datos
        //  4. enviar el token de validacion en un correo de bienvenida
        //  5. agregar al usuario a una cola de verificacion manual
        return userDao.create(username, mail);
    }

    public User registerUser(final String username) {
        return null;
    }

}
