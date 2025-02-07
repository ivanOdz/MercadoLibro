package ar.edu.itba.paw.webapp.dto.input;

import javax.validation.constraints.NotEmpty;

public class VerificationDTO {

    @NotEmpty
    private int verificationCode;

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }
}
