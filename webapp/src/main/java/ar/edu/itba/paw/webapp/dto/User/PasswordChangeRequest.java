package ar.edu.itba.paw.webapp.dto.User;

import javax.validation.constraints.NotEmpty;

public class PasswordChangeRequest {

    @NotEmpty
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

