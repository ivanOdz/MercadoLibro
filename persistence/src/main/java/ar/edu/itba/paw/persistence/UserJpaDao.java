package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Primary
@Repository
public class UserJpaDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public User createUser(String username, String mail, String password, String language, int verificationCode) {
        final User user = new User(null, username, mail, password,null,verificationCode, false, language);
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("FROM User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        try{
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    @Override
    public Optional<User> findByMail(String mail) {
        final TypedQuery<User> query = em.createQuery("FROM User as u where u.mail = :mail", User.class);
        query.setParameter("mail", mail);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<User> getUserToVerify(int verificationCode) {
        final TypedQuery<User> query = em.createQuery("FROM User as u where u.verificationCode = :verificationCode", User.class);
        query.setParameter("verificationCode", verificationCode);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public void setUserLanguage(User user, String language) {
            user.setLanguage(language);
    }

    @Override
    public void setUserFavoriteLocation(User user, Location favoriteLocation) {
            user.setFavoriteLocation(favoriteLocation);
    }
    
    @Override
    public void addUserLocation(User user, Location location) {
        user.addLocation(location);
        em.merge(user);
    }

    @Override
    public void removeUserLocation(User user, Location location) {
            user.removeLocation(location);
            em.merge(user);
    }
    
    @Override
    public void verifyUser(User user) {
        user.setVerified(true);
        user.setVerificationCode(null);
    }


    @Override
    public void changePasswordSolicited(User user, int verificationCode) {
            user.setVerificationCode(verificationCode);
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }

    @Override
    public boolean updateUsername(User user, String newUsername) {
        user.setUsername(newUsername);
        return true;
    }
}
