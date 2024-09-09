package ar.edu.itba.paw.models;


import ar.edu.itba.paw.models.utils.PublicationState;

import java.sql.Timestamp;

public class Publication {
    private final long publicationId;
    private final long bookId;
    private final long userId;
    private final PublicationState publicationState;
    private final Timestamp publicationDatetime;
    private final long locationId;

    // Constructor
    public Publication(long publicationId, long bookId, long userId, PublicationState publicationState, Timestamp publicationDatetime, long locationId) {
        this.publicationId = publicationId;
        this.bookId = bookId;
        this.userId = userId;
        this.publicationState = publicationState;
        this.publicationDatetime = publicationDatetime;
        this.locationId = locationId;
    }

    public long getPublicationId() {
        return publicationId;
    }

    public long getBookId() {
        return bookId;
    }

    public long getUserId() {
        return userId;
    }

    public PublicationState getPublicationState() {
        return publicationState;
    }

    public Timestamp getPublicationDatetime() {
        return publicationDatetime;
    }

    public long getLocationId() {
        return locationId;
    }
}
