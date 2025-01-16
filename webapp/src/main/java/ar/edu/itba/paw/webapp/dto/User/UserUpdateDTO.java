package ar.edu.itba.paw.webapp.dto.User;

import ar.edu.itba.paw.webapp.validation.SupportedLanguage;
import ar.edu.itba.paw.webapp.validation.UniqueUsername;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserUpdateDTO {

    @SupportedLanguage
    private String language;

    @UniqueUsername
    @Pattern(regexp = "^[a-zA-Z0-9+]+$")
    @Size(min = 5, max = 100)
    private String newUsername;


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}
