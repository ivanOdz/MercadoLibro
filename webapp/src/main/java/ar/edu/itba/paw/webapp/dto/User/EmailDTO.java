package ar.edu.itba.paw.webapp.dto.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmailDTO {

    @Email
    @NotNull
    @Size(min = 15, max = 100)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

