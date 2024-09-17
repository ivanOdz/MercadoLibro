package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    User createUser(String username, String mail, String password, int verificationCode);
    
    Optional<User> find(String mail);

    Optional<User> findByUsername(String username);

    void verifyUser(int verificationCode);

    void changePasswordSolicited(String email, int verificationCode);

    void changePassword(int verificationCode, String newPassword);

    User getUserByPubId(long pubId);
    
    boolean updateUsername(long userId, String newUsername);
}
