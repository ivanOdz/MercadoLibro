package ar.edu.itba.paw.models;

public class User {

    private final long userId;
    private final String username;
    private final String mail;
    private final String password;
    private final Long imageId;
    private final Integer verificationCode;
    private final boolean isVerified;

    public User(long userId, String username, String mail, String password, Long imageId, Integer verificationCode, boolean isVerified) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.imageId = imageId;
        this.verificationCode = verificationCode;
        this.isVerified = isVerified;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
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
