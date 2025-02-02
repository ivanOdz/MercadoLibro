package ar.edu.itba.paw.webapp.dto.input;

import javax.validation.constraints.NotEmpty;

public class PasswordChangeDTO {

    @NotEmpty
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

