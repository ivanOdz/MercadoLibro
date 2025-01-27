package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {
	
    Optional<User> findById(long id);

    Optional<User> findByMail(String mail);

    Optional<User> findByUsername(String username);

    Optional<User> findByVerificationCode(int verificationCode);

    void verifyUser(User user);

    User createUser(String username, String mail, String password, String language, int verificationCode);

    void changePasswordSolicited(User user, int verificationCode);

    void changePassword(User user, String newPassword);

    User updateUsername(User user, String newUsername);

    void setUserLanguage(User user, String language);
    
    void setUserFavoriteLocation(User user, Location favoriteLocation);
    
    void addUserLocation(User user, Location location);
    
    void removeUserLocation(User user, Location location);
}
