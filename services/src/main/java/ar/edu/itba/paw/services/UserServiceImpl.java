package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.*;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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

    @Override
    @Transactional
    public User createUser(String username, String mail, String password, String language) {
        Optional<User> u = userDao.findByMail(mail);

        if(u.isPresent()) {
            return u.get();
        }
        
        User user = userDao.createUser(username, mail, passwordEncoder.encode(password), language, generateVerificationCode());

        emailService.sendVerificationEmail(user);
        return user;
    }

    @Override
    @Transactional
    public void changePassword(int verificationCode, String newPassword) {
        User user = getUserToVerify(verificationCode);
        userDao.changePassword(user,passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public void changePasswordSolicited(String email) {
        Optional<User> user = userDao.findByMail(email);
        if(user.isEmpty()){
            throw new PasswordChangeBadRequestException("User not found");
        }

        int verificationCode = generateVerificationCode();
        userDao.changePasswordSolicited(user.get(), verificationCode);
        emailService.sendPasswordChangeEmail(user.get());
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(long id) {
        Optional<User> user = userDao.findById(id);
        if(user.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return user.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String mail) {
        return userDao.findByMail(mail);
    }

    @Override
    @Transactional(readOnly = true)
    public String findUsernameByEmail(String mail){
        return findUserByEmail(mail).map(User::getUsername).orElse("");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional
    public void verifyUser(int verificationCode) {
        User user = getUserToVerify(verificationCode);
        userDao.verifyUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String mail) {
        return userDao.findByMail(mail).isPresent();
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
        Optional<User> user = userDao.findById(userId);
        if(user.isEmpty()){
            throw new UserModifyBadRequestException("Error modifying user: User not found");
        }
    	return userDao.updateUsername(user.get(), newName);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserToVerify(int verificationCode) {
        Optional<User> user = userDao.getUserToVerify(verificationCode);
        if(user.isEmpty()){
            throw new UserVerificationBadRequestException("User verification failed");
        }
        return user.get();
    }

    @Override
    @Transactional
    public void setUserLanguage(User user, String language) {
        userDao.setUserLanguage(user,language);
    }
    
    @Override
    @Transactional
    public void addLocation(Long userId, String locationString) {
        User user = findById(userId);
        boolean locationExists = false;
        
        for (Location existingLocation : user.getUserLocations()) {
            if (existingLocation.getLocationString().equals(locationString)) {
                locationExists = true;
                break;
            }
        }
        
        if (!locationExists)
        {
	        Location newLocation = locationService.newLocation(locationString);
	        userDao.addUserLocation(user, newLocation);
        }
    }
    
    @Override
    @Transactional
    public void removeLocation(Long userId, Long locationId) {
        Location location = locationService.findById(locationId);
        userDao.removeUserLocation(findById(userId), location);
    }

}
