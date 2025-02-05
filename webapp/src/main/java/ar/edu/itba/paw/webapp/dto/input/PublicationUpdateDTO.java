package ar.edu.itba.paw.webapp.dto.input;


import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;

public class PublicationUpdateDTO {

    private URI locationURN;

    public URI getLocationURN() {
        return locationURN;
    }
    public Long getLocationId() {
        return UrnResolverUtil.getLocationId(locationURN);
    }

    public void setLocationURN(URI locationURN) {
        this.locationURN = locationURN;
    }
}
