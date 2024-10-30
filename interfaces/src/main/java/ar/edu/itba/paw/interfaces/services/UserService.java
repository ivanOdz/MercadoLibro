package ar.edu.itba.paw.interfaces.services;


import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserReview;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {

    Optional<User> findById(long id);

    Optional<User> findUserByEmail(String mail);

    String findUsernameByEmail(String mail);

    User createUser(String username, String mail, String password, String language);

    Optional<User> findByUsername(String username);

    void verifyUser(int verificationCode);

    void changePasswordSolicited(String email);
    
    void changePassword(int verificationCode, String newPassword);

    //List<UserReview> getReviewsByUserId(long userId, int pageIndex);

    boolean userExists(String mail);
    
    boolean changeUserName(long userId, String newName);

    User getUserToVerify(int verificationCode);

    //Double getUserRating(long userId);

    void setUserLanguage(User user, String language);
    
    public void addLocation(Long userId, String locationString);
    
    public void removeLocation(Long userId, Long locationId);
}
