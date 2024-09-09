package ar.edu.itba.paw.models;

public class User {

    private final long userId;
    private String username;
    private final String mail;
    private final String password;
    private final int verificationCode;
    private final boolean isVerified;

    public User(long userId, String username, String mail, String password, int verificationCode, boolean isVerified) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.verificationCode = verificationCode;
        this.isVerified = isVerified;
    }

    public long getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public int getVerificationCode(){
        return verificationCode;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        //TODO
        return password;
    }
}
