package ar.edu.itba.paw.models;

public class User {

    private final long id;
    private final String username;
    private final String mail;

    public User(long id, String username, String mail) {
        this.id = id;
        this.username = username;
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }
}
