package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User findById(long id);

    User createUser(String username, String mail, String password, String language);

    User updateUser(Long id, String language, String newUsername);

    Optional<User> findByUsername(String username);

    User verifyUser(int verificationCode);

    Integer changePasswordSolicited(String email);

    User changePassword(int verificationCode, String newPassword);

    boolean userExists(String mail);
    
    User changeUsername(long userId, String newName);

    User setUserLanguage(long userId, String language);

    Location getLocation(long locationId);

    List<Location> getLocations(long userId, Integer publicationId);
    
    Location addLocation(Long userId, String locationString);
    
    void removeLocation(Long userId, Long locationId);
}
