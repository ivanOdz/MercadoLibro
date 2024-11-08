package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.EmailService;
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
    private LocationDao locationDao;

    @Transactional
    @Override
    public User createUser(String username, String mail, String password, String language) {
        Optional<User> u = userDao.findByMail(mail);
        
        if(u.isPresent()) {
            return u.get();
        }
        
        User user = userDao.createUser(username, mail, passwordEncoder.encode(password), language, generateVerificationCode());

        emailService.sendVerificationEmail(user);
        return user;
    }

    @Transactional
    @Override
    public void changePassword(int verificationCode, String newPassword) {
        userDao.changePassword(verificationCode,passwordEncoder.encode(newPassword));
    }

    @Transactional
    @Override
    public void changePasswordSolicited(String email) {
        int verificationCode = generateVerificationCode();

        userDao.changePasswordSolicited(email, verificationCode);
        User u = getUserToVerify(verificationCode);

        emailService.sendPasswordChangeEmail(u);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findUserByEmail(String mail) {
        return userDao.findByMail(mail);
    }

    @Transactional(readOnly = true)
    @Override
    public String findUsernameByEmail(String mail){
        return findUserByEmail(mail).map(User::getUsername).orElse("");
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    @Override
    public void verifyUser(int verificationCode) {
        userDao.verifyUser(verificationCode);
    }

    @Transactional(readOnly = true)
    @Override
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

    @Transactional
    @Override
    public boolean changeUserName(long userId, String newName) {
    	return userDao.updateUsername(userId, newName);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserToVerify(int verificationCode) {
        return userDao.getUserToVerify(verificationCode);
    }

    @Transactional
    @Override
    public void setUserLanguage(User user, String language) {
        userDao.setUserLanguage(user.getUserId(),language);
    }

    @Transactional
    @Override
    public void addLocation(Long userId, String locationString) {

        Optional<User> userOptional = userDao.findById(userId);
        
        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();
        boolean locationExists = false;
        
        for (Location existingLocation : user.getUserLocations()) {
            if (existingLocation.getLocationString().equals(locationString)) {
                locationExists = true;
                break;
            }
        }
        
        if (!locationExists)
        {
	        Location newLocation = locationDao.newLocation(locationString);
	        userDao.addUserLocation(userId, newLocation);
        }
    }

    @Transactional
    @Override
    public void removeLocation(Long userId, Long locationId) {
    	
        Optional<User> userOptional = userDao.findById(userId);
        
        if (userOptional.isEmpty()) {
            return;
        }

        Optional<Location> locationOptional = locationDao.findById(locationId);
        
        if (locationOptional.isEmpty()) {
            return;
        }

        Location location = locationOptional.get();
        
        userDao.removeUserLocation(userId, location);
    }

}
