package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.*;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Primary
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LocationService locationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    public User createUser(String username, String mail, String password, String language) {
        Optional<User> u = userDao.findByMail(mail);

        if(u.isPresent()) {
            LOGGER.warn("Attempted to create a user with an already registered email, associated to user id: {}", u.get().getUserId());
            return u.get();
        }
        
        User user = userDao.createUser(username, mail, passwordEncoder.encode(password), language, generateVerificationCode());

        LOGGER.info("New user created with id: {}", user.getUserId());
        emailService.sendVerificationEmail(user);

        return user;
    }

    @Override
    @Transactional
    public void changePassword(int verificationCode, String newPassword) {
        LOGGER.info("Password change request received.");

        User user = getUserToVerify(verificationCode);
        userDao.changePassword(user,passwordEncoder.encode(newPassword));

        LOGGER.info("Password changed successfully for user with ID: {}", user.getUserId());
    }

    @Override
    @Transactional
    public void changePasswordSolicited(String email) {
        // Not logging user email, as it is sensitive information
        LOGGER.info("Password change request received.");

        Optional<User> user = userDao.findByMail(email);
        if(user.isEmpty()){
            LOGGER.warn("User not found for email, attempt made.");
            throw new PasswordChangeBadRequestException("User not found");
        }

        int verificationCode = generateVerificationCode();
        userDao.changePasswordSolicited(user.get(), verificationCode);
        LOGGER.info("Verification code generated and saved for user with ID: {}", user.get().getUserId());

        emailService.sendPasswordChangeEmail(user.get());
        LOGGER.info("Password change email sent to user with ID: {}", user.get().getUserId());
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(long id) {
        LOGGER.info("Searching for user with ID: {}", id);

        Optional<User> user = userDao.findById(id);
        if(user.isEmpty()){
            LOGGER.warn("User with ID {} not found", id);
            throw new UserNotFoundException("User not found");
        }

        LOGGER.info("User with ID {} found", id);
        return user.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        LOGGER.info("Entering the service to look up user: {}", username);

        Optional<User> user = userDao.findByUsername(username);

        if (user.isPresent()) {
            LOGGER.info("User of id {} found for username: {}", user.get().getUserId(), username);
        } else {
            LOGGER.warn("No user found for {}", username);
        }

        return user;
    }

    @Override
    @Transactional
    public void verifyUser(int verificationCode) {
        LOGGER.info("Initiating user verification process.");

        User user = getUserToVerify(verificationCode);

        if (user != null) {
            LOGGER.info("User of ID {} found for verification. Proceeding with verification.", user.getUserId());
            userDao.verifyUser(user);
            LOGGER.info("User verification completed successfully.");
        } else {
            LOGGER.warn("User verification failed. No user found for provided verification code.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String mail) {
        LOGGER.info("Checking if a user exists based on provided email.");

        boolean exists = userDao.findByMail(mail).isPresent();

        if (exists) {
            LOGGER.info("User exists for provided email.");
        } else {
            LOGGER.info("No user found for provided email.");
        }

        return exists;
    }


    /**
     * Generates random verification code when verifying user or updating password
     * @return verification code
     */
    private int generateVerificationCode(){
        Random random = new Random();
        return Math.abs(random.nextInt());
    }

    @Override
    @Transactional
    public boolean changeUserName(long userId, String newName) {
        LOGGER.info("Request to change username received for user ID: {}", userId);

        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()) {
            LOGGER.warn("Failed to change username of user ID {}", userId);
            throw new UserModifyBadRequestException("Error modifying user: User not found");
        }

        boolean result = userDao.updateUsername(user.get(), newName);
        if (result) {
            LOGGER.info("Username successfully updated for user ID: {}", userId);
        } else {
            LOGGER.warn("Failed to update username for user ID: {}", userId);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserToVerify(int verificationCode) {
        LOGGER.info("Attempting to verify user with provided verification code.");

        Optional<User> user = userDao.getUserToVerify(verificationCode);
        if (user.isEmpty()) {
            LOGGER.warn("User verification failed: no user found for provided verification code.");
            throw new UserVerificationBadRequestException("User verification failed");
        }

        LOGGER.info("User verification successful.");
        return user.get();
    }

    @Override
    @Transactional
    public void setUserLanguage(User user, String language) {
        LOGGER.info("Initiating language update for user with ID: {}", user.getUserId());

        userDao.setUserLanguage(user, language);

        LOGGER.info("Language {} successfully updated for user with ID: {}", language, user.getUserId());
    }

    @Override
    @Transactional
    public void addLocation(Long userId, String locationString) {
        LOGGER.info("Attempting to add a location for user with ID: {}", userId);

        User user = findById(userId);
        boolean locationExists = false;

        for (Location existingLocation : user.getUserLocations()) {
            if (existingLocation.getLocationString().equals(locationString)) {
                LOGGER.info("Location already exists for user with ID: {}", userId);
                locationExists = true;
                break;
            }
        }

        if (!locationExists) {
            Location newLocation = locationService.newLocation(locationString);
            userDao.addUserLocation(user, newLocation);
            LOGGER.info("New location added for user with ID: {}", userId);
        }
    }


    @Override
    @Transactional
    public void removeLocation(Long userId, Long locationId) {
        LOGGER.info("Attempting to remove location with ID: {} for user with ID: {}", locationId, userId);

        Location location = locationService.findById(locationId);
        if (location != null) {
            userDao.removeUserLocation(findById(userId), location);
            LOGGER.info("Location with ID: {} successfully removed for user with ID: {}", locationId, userId);
        } else {
            LOGGER.warn("Location with ID: {} not found for removal for user with ID: {}", locationId, userId);
        }
    }

}
