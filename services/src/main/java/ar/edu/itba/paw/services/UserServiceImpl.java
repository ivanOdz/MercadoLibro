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
    private MessageSource messageSource;

    //@Autowired
    //private final UserReviewService userReviewsService;

    @Autowired
    private LocationDao locationDao;
    
    @Value("#{environment.webappUrl}")
    private String webappUrl;


    @Transactional
    @Override
    public User createUser(String username, String mail, String password, String language) {
        // 1. validar inputs
        // 2. ingresarlo en la base de datos
        // 3. generar un Token de validacion y guardarlo en la base de datos
        // 4. enviar el token de validacion en un correo de bienvenida
        // 5. agregar al usuario a una cola de verificacion manual
        Optional<User> u = userDao.findByMail(mail);
        
        if(u.isPresent()) {
            return u.get();
        }
        
        User user = userDao.createUser(username, mail, passwordEncoder.encode(password), language, generateVerificationCode());

        emailService.sendVerificationEmail(user);
        return user;
    }

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

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String mail) {
        return userDao.findByMail(mail);
    }

    @Override
    public String findUsernameByEmail(String mail){
        return findUserByEmail(mail).map(User::getUsername).orElse("");
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    @Override
    public void verifyUser(int verificationCode) {
        userDao.verifyUser(verificationCode);
    }

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
    
    @Override
    public boolean changeUserName(long userId, String newName) {
    	return userDao.updateUsername(userId, newName);
    }

    @Override
    public User getUserToVerify(int verificationCode) {
        return userDao.getUserToVerify(verificationCode);
    }

    @Override
    public void setUserLanguage(User user, String language) {
        userDao.setUserLanguage(user.getUserId(),language);
    }
    
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
