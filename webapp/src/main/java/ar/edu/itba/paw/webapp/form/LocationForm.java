package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class LocationForm {
    private Long locationId;
    private Long publicationId;

    public void setLocationId(@NotNull Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public void setLocation(@NotNull long locationId) {
        this.locationId = locationId;
    }

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }
}
