package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.utils.UrnResolverUtil;

import java.net.URI;

public class PublicationUpdateDTO {

    private URI locationURN;

    public Long getLocationURN() {
        return UrnResolverUtil.getLocationId(locationURN);
    }

    public void setLocationURN(URI locationURN) {
        this.locationURN = locationURN;
    }
}
