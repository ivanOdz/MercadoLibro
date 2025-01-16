package ar.edu.itba.paw.webapp.dto.User;

import ar.edu.itba.paw.webapp.validation.UniqueEmail;
import ar.edu.itba.paw.webapp.validation.UniqueUsername;

import javax.validation.constraints.*;

public class RegisterForm {

    @UniqueUsername
    @Pattern(regexp = "^[a-zA-Z0-9+]+$")
    @Size(min = 5, max = 100)
    private String username;

    @UniqueEmail
    @NotBlank
    @Size(min = 15, max = 100)
    @Email
    private String mail;

    @NotEmpty
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
