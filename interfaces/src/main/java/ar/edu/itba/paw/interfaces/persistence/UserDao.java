package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {
	
    Optional<User> findById(long id);

    User createUser(String username, String mail, String password, String language, int verificationCode);
    
    Optional<User> findByMail(String mail);

    Optional<User> findByUsername(String username);

    void verifyUser(User user);

    void changePasswordSolicited(User user, int verificationCode);

    void changePassword(User user, String newPassword);

    boolean updateUsername(User user, String newUsername);

    Optional<User> getUserToVerify(int verificationCode);

    void setUserLanguage(User user, String language);
    
    void setUserFavoriteLocation(User user, Location favoriteLocation);
    
    void addUserLocation(User user,Location location);
    
    void removeUserLocation(User user, Location location);
}
