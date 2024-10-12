package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "users_userid_seq")
    @SequenceGenerator(sequenceName = "users_userid_seq", name = "users_userid_seq", allocationSize = 1)
    @Column(name = "userid")
    private Long userId;

    @Column(length = 64, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false, insertable = true, updatable = true)
    private String password;

    @Column(name = "imageid")
    private Long imageId;

    @Column(name = "verificationcode")
    private Integer verificationCode;

    @Column(name = "isverified")
    private boolean isVerified;

    @Column(length = 64, nullable = false)
    private String language;

    private String mail;


    /* package */User(){
        // only for JPA
    }

    public User(Long userId, String username, String mail, String password, Long imageId, Integer verificationCode, boolean isVerified, String language) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.imageId = imageId;
        this.verificationCode = verificationCode;
        this.isVerified = isVerified;
        this.language = language;
    }

    public long getUserId() {
        return userId;
    }

    public String getLanguage() {
        return language;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        // TODO:
        return password;
    }

    public Long getImageId() {
        return imageId;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public boolean isVerified() {
        return isVerified;
    }
}
