package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserService {

    User findById(long id);

    User createUser(String username, String mail, String password, String language);

    Optional<User> findByUsername(String username);

    User verifyUser(int verificationCode);

    Integer changePasswordSolicited(String email);

    User changePassword(int verificationCode, String newPassword);

    boolean userExists(String mail);
    
    boolean changeUsername(long userId, String newName);

    void setUserLanguage(long userId, String language);
    
    Location addLocation(Long userId, String locationString);
    
    void removeLocation(Long userId, Long locationId);
}
