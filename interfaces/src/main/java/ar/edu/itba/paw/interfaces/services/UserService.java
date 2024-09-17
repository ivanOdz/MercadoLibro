package ar.edu.itba.paw.interfaces.services;


import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserReview;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    Optional<User> findById(long id);

    Optional<User> findUserByEmail(String mail);

    String findUsernameByEmail(String mail);

    User createUser(String username, String mail, String password);

    Optional<User> findByUsername(String username);

    void verifyUser(int verificationCode);

    void changePasswordSolicited(String email);
    
    void changePassword(int verificationCode, String newPassword);

    List<UserReview> getReviewsByUserId(long userId);

    User getUserByPubId(long pubId);

    boolean userExists(String mail);
    
    boolean changeUserName(long userId, String newName);
}
