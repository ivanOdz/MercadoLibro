package ar.edu.itba.paw.models;

public class User {

    private final long userId;
    private final String username;
    private final String mail;
    private final String password;

    public User(long userId, String username, String mail, String password) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
        this.password = password;
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

    public String getPassword() {
        //TODO
        return password;
    }
}
