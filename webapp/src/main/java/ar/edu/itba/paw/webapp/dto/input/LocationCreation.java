package ar.edu.itba.paw.webapp.dto.input;

import javax.validation.constraints.NotEmpty;

public class LocationCreation {

    @NotEmpty
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
