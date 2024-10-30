package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {
	
    Optional<User> findById(long id);

    User createUser(String username, String mail, String password, String language, int verificationCode);
    
    Optional<User> findByMail(String mail);

    Optional<User> findByUsername(String username);

    void verifyUser(int verificationCode);

    void changePasswordSolicited(String email, int verificationCode);

    void changePassword(int verificationCode, String newPassword);


    boolean updateUsername(long userId, String newUsername);

    User getUserToVerify(int verificationCode);

    //List<UserReview> getReviewsByUserId(long userId, int pageIndex);

    //Double getUserRating(long userId);

    void setUserLanguage(long userId, String language);
    
    public void setUserFavoriteLocation(long userId, Location favoriteLocation);
    
    public void addUserLocation(long userId, Location location);
}
