package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserService {

    User findById(long id);

    User createUser(String username, String mail, String password, String language);

    Optional<User> findByUsername(String username);

    User verifyUser(int verificationCode);

    void changePasswordSolicited(String email);

    User changePassword(int verificationCode, String newPassword);

    boolean userExists(String mail);
    
    boolean changeUserName(long userId, String newName);

    void setUserLanguage(User user, String language);
    
    void addLocation(Long userId, String locationString);
    
    void removeLocation(Long userId, Long locationId);
}
