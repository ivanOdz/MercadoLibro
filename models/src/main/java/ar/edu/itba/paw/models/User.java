package ar.edu.itba.paw.models;

public class User {

    private final long userId;
    private String username;
    private final String mail;

    public User(long userId, String username, String mail) {
        this.userId = userId;
        this.username = username;
        this.mail = mail;
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

    public void setUsername(String username) {
        this.username = username;
    }
}
