package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.exceptions.*;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Primary
@Repository
public class UserJpaDao implements UserDao {

    @Autowired
    MessageSource messageSource;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public User createUser(String username, String mail, String password, String language, int verificationCode) {

        final User user = new User(null, username, mail, password,null,verificationCode, false, language);

        // NOTE: consultar por manejo de excepciones en JPA

        /*
        Number userId;
        try {
            userId = jdbcInsert.executeAndReturnKey(userData);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = messageSource.getMessage("error.createUser", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new UserBadRequestException(errorMessage);
        }
        */

        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("FROM User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        return Optional.ofNullable(query.getSingleResult());
    }


    @Override
    public Optional<User> findByMail(String mail) {
        // consulta JQL no SQL
        final TypedQuery<User> query = em.createQuery("FROM User as u where u.mail = :mail", User.class);
        query.setParameter("mail", mail);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public User getUserToVerify(int verificationCode) {
        final TypedQuery<User> query = em.createQuery("FROM User as u where u.verificationCode = :verificationCode", User.class);
        query.setParameter("verificationCode", verificationCode);

        if (Optional.ofNullable(query.getResultList()).isEmpty()) {
            String errorMessage = messageSource.getMessage("error.userToVerifyNotFound", new Object[]{verificationCode}, LocaleContextHolder.getLocale());
            throw new UserNotFoundException(errorMessage);
        }

        return query.getSingleResult();
    }


    // ------- sin implementar con hibernate


    @Override
    public boolean updateUsername(long userId, String newUsername) {

        int rowsAffected;
        try {
            rowsAffected = jdbcTemplate.update("UPDATE users SET userName = ? WHERE userId = ? AND NOT EXISTS (SELECT * FROM users WHERE userName = ?)", new Object[]{newUsername, userId, newUsername}, new int[]{Types.VARCHAR, Types.BIGINT, Types.VARCHAR});
        } catch (DataIntegrityViolationException e) {
            String errorMessage = messageSource.getMessage("error.changeUserName", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new UserModifyBadRequestException(errorMessage);
        }
        return rowsAffected >= 1;
    }

    @Override
    public void setUserLanguage(long userId, String language) {
        try{
            jdbcTemplate.update("UPDATE users SET language = ? WHERE userId = ?", new Object[]{language, userId},
                    new int[]{Types.VARCHAR, Types.BIGINT});
        }catch (DataIntegrityViolationException e){
            String errorMessage = messageSource.getMessage("error.setUserLanguage", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new UserModifyBadRequestException(errorMessage);
        }
    }

    @Override
    public void verifyUser(int verificationCode) {
        try {
            jdbcTemplate.update("UPDATE users SET isVerified = ? WHERE verificationCode = ?", new Object[]{true, verificationCode},
                    new int[]{Types.BOOLEAN, Types.INTEGER});
            jdbcTemplate.update("UPDATE users SET verificationCode = ? WHERE verificationCode = ?", new Object[]{null, verificationCode},
                    new int[]{Types.NULL, Types.INTEGER});

        } catch (DataIntegrityViolationException e) {
            String errorMessage = messageSource.getMessage("error.verifyUser", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new UserVerificationBadRequestException(errorMessage);
        }
    }

    @Override
    public void changePasswordSolicited(String email, int verificationCode) {
        try {
            jdbcTemplate.update("UPDATE users SET verificationCode = ? WHERE mail = ?", new Object[]{verificationCode, email},
                    new int[]{Types.INTEGER, Types.VARCHAR});
        } catch (DataIntegrityViolationException e) {
            String errorMessage = messageSource.getMessage("error.changePasswordSolicited", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new PasswordChangeBadRequestException(errorMessage);
        }
    }

    @Override
    public void changePassword(int verificationCode, String newPassword) {
        try {
            jdbcTemplate.update("UPDATE users SET password = ? WHERE verificationCode = ?", new Object[]{newPassword, verificationCode},
                    new int[]{Types.VARCHAR, Types.INTEGER});
        } catch (DataIntegrityViolationException e) {
            String errorMessage = messageSource.getMessage("error.changePassword", new Object[]{e.getStackTrace()}, LocaleContextHolder.getLocale());
            throw new PasswordChangeBadRequestException(errorMessage);
        }
    }
}
