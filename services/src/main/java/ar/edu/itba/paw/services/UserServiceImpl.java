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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
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
    private static final int MAX_LOCATIONS_PER_USER = 5;

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
    public User updateUser(Long id, String language, String newUsername) {
        User user = userDao.findById(id).orElseThrow(() -> new UserNotFoundException("No such user"));
        if(language != null){
            user = setUserLanguage(user.getUserId(), language);
        }
        if(newUsername != null){
            user = changeUsername(user.getUserId(), newUsername);
        }
        return user;
    }

    @Override
    @Transactional
    public User changePassword(int verificationCode, String newPassword) {
        LOGGER.info("Password change request received.");

        Optional<User> user = userDao.findByVerificationCode(verificationCode);
        if (user.isPresent()) {
            userDao.changePassword(user.get(), passwordEncoder.encode(newPassword));

            LOGGER.info("Password changed successfully for user with ID: {}", user.get().getUserId());
        }
        else {
            throw new UserVerificationBadRequestException("User verification failed");
        }
        return user.get();
    }

    @Override
    @Transactional
    public Integer changePasswordSolicited(String email) {
        LOGGER.info("Password change request received.");

        Optional<User> user = userDao.findByMail(email);
        if(user.isEmpty()){
            LOGGER.warn("User not found when attempting to change password");
            throw new UserNotFoundException("User not found for email: \"" + email + "\"");
        }

        int passwordCode = generateVerificationCode();
        userDao.changePasswordSolicited(user.get(), passwordCode);
        LOGGER.info("Verification code generated and saved for user with ID: {}", user.get().getUserId());

        emailService.sendPasswordChangeEmail(user.get());
        LOGGER.info("Password change email sent to user with ID: {}", user.get().getUserId());
        return passwordCode;
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
    public User verifyUser(int verificationCode) {
        LOGGER.info("Initiating user verification process.");

        Optional<User> user = userDao.findByVerificationCode(verificationCode);
        if (user.isEmpty()) {
            LOGGER.warn("User verification failed: no user found for provided verification code.");
            throw new UserVerificationBadRequestException("User verification failed");
        }

        LOGGER.info("User of ID {} found for verification. Proceeding with verification.", user.get().getUserId());
        userDao.verifyUser(user.get());
        LOGGER.info("User verification completed successfully.");

        return user.get();
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
    public User changeUsername(long userId, String newName) {
        LOGGER.info("Request to change username received for user ID: {}", userId);

        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()) {
            LOGGER.warn("Failed to change username of user ID {}", userId);
            throw new UserModifyBadRequestException("Error modifying user: User not found");
        }

        User updatedUser = userDao.updateUsername(user.get(), newName);

        LOGGER.info("Username successfully updated for user ID: {}", userId);

        return updatedUser;
    }

    @Override
    @Transactional
    public User setUserLanguage(long userId, String language) {
        LOGGER.info("Initiating language update for user with ID: {}", userId);

        User user = findById(userId);
        userDao.setUserLanguage(user, language);

        LOGGER.info("Language {} successfully updated for user with ID: {}", language, user.getUserId());
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> getLocations(Long userId, Long publicationId) {
        if(publicationId == null){
            return findById(userId).getUserLocations().stream().toList();
        }

        return locationService.getLocationByPublicationId(publicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Location getLocation(long locationId) {
        return locationService.findById(locationId);
    }



    @Override
    @Transactional
    public Location addLocation(Long userId, String locationString) {
        LOGGER.info("Attempting to add a location for user with ID: {}", userId);

        User user = findById(userId);

        if(user.getUserLocations().size() >= MAX_LOCATIONS_PER_USER){
            LOGGER.warn("Location could not be added. Maximum amount of location for user {} reached.", userId);
            throw new LocationMaximumAmountBadRequestException("Maximum amount of location reached");
        }

        Location newLocation =  locationService.newLocation(locationString);
        userDao.addUserLocation(user, newLocation);

        LOGGER.info("New location added for user with ID: {}", userId);

        return newLocation;
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

    @Override
    public User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
