package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class MailForm {

    @NotEmpty(message = "{mailForm.email.notEmpty}")
    @Email(message = "{Email.email.invalidFormat}")
    private String email;

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
