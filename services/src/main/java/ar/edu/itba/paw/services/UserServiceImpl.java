package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
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

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MessageSource messageSource;
    //private final UserReviewService userReviewsService;

    @Value("#{environment.webappUrl}")
    private String webappUrl;

    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, final EmailService emailService, final MessageSource messageSource) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.messageSource = messageSource;
    }

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
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", user.getUsername());
        variables.put("validationUrl", webappUrl + "/verification?verification_code=" + user.getVerificationCode());
        emailService.sendEmail(user.getMail(), variables, "verification", messageSource.getMessage("email.subject.verification", null, Locale.forLanguageTag(user.getLanguage())), user.getLanguage());

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

        Map<String, Object> variables = new HashMap<>();
        variables.put("validationUrl", webappUrl +"/change_password?verification_code=" + verificationCode);

        String locale = u.getLanguage() != null ? u.getLanguage() : Locale.getDefault().getLanguage();

        emailService.sendEmail(email, variables, "changePassword", messageSource.getMessage("email.subject.passwordChange", null, Locale.forLanguageTag(locale)), locale);
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

/*
    // Mudar la logica al UserReviewService.
    @Override
    public List<UserReview> getReviewsByUserId(long userId, int pageIndex) {
        return userDao.getReviewsByUserId(userId, pageIndex);
    }
    // Mudar la logica al UserReviewService.
    @Override
    public Double getUserRating(long userId) {
        return userDao.getUserRating(userId);
    }*/

    @Override
    public void setUserLanguage(User user, String language) {
        userDao.setUserLanguage(user.getUserId(),language);
    }
}
