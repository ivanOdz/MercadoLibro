package ar.edu.itba.paw.models;

public class Location {
    private final long locationId;
    private final String locationString;

    public Location(long locationId, String locationString) {
        this.locationId = locationId;
        this.locationString = locationString;
    }

    public long getLocationId() {
        return locationId;
    }

    public String getLocationString() {
        return locationString;
    }
}
