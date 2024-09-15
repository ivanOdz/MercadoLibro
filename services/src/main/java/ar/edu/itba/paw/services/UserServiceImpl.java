package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.UserReview;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Primary
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserReviewService userReviewsService;


    @Value("#{environment.webappUrl}")
    private String webappUrl;

    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, final EmailService emailService, final UserReviewService userReviewsService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userReviewsService = userReviewsService;
    }
    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String mail) {
        return userDao.find(mail);
    }

    @Override
    public String findUsernameByEmail(String mail){
        return findUserByEmail(mail).map(User::getUsername).orElse("");
    }

    @Override
    public User createUser(String username, String mail, String password) {
        //register user
        //TODO
        //  1. validar inputs
        //  2. ingresarlo en la base de datos
        //  3. generar un Token de validacion y guardarlo en la base de datos
        //  4. enviar el token de validacion en un correo de bienvenida
        //  5. agregar al usuario a una cola de verificacion manual


        User user = userDao.createUser(username, mail, passwordEncoder.encode(password), generateVerificationCode());
        if(user == null) {
            return null; // user exists -> returns null
        }

        Map<String, Object> variables = new HashMap<>();

        variables.put("username", user.getUsername());
        variables.put("validationUrl", webappUrl + "/verification?verification_code=" + user.getVerificationCode());

        emailService.sendEmail(user.getMail(), variables, "verification", "User verification");

        return user;

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void verifyUser(int verificationCode) {
        userDao.verifyUser(verificationCode);
    }

    @Override
    public void changePasswordSolicited(String email) {
        int verificationCode = generateVerificationCode();
        userDao.changePasswordSolicited(email, verificationCode);

        Map<String, Object> variables = new HashMap<>();

        variables.put("validationUrl", webappUrl +"/change_password?verification_code=" + verificationCode);

        emailService.sendEmail(email, variables, "changePassword", "Password change");
    }

    @Override
    public void changePassword(int verificationCode, String newPassword) {
        userDao.changePassword(verificationCode,passwordEncoder.encode(newPassword));
    }

    @Override
    public List<UserReview> getReviewsByUserId(long userId) {
        return userReviewsService.getReviewsByUserId(userId);
    }

    @Override
    public User getUserByPubId(long pubId) {
        return userDao.getUserByPubId(pubId);
    }

    @Override
    public boolean userExists(String mail) {
        return userDao.find(mail).isPresent();
    }

//    @Override
//    public void changePassword(String email, String newPassword) {
//        userDao.changePassword(passwordEncoder.encode(newPassword))
//    }


    /**
     * Generates random verification code when verifying user or updating password
     * @return verification code
     */
    private int generateVerificationCode(){
        Random random = new Random();
        return Math.abs(random.nextInt());
    }
}
