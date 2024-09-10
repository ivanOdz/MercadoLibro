package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.AssertTrue;

public class PasswordForm {
    private String password;
    private String confirmPassword;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @AssertTrue(message = "{userForm.passwords.mismatch}")
    public boolean isPasswordsMatching() {
        return password != null && password.equals(confirmPassword);
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
