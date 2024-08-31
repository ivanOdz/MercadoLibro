package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.PublicationState;

public class Publication {

    private final long publicationId;
    private final long bookId;
    private final long userId;
    private int publicationState;
    private final String location;

    public Publication(long publicationId, long bookId, long userId, String location) {
        this.publicationId = publicationId;
        this.bookId = bookId;
        this.userId = userId;
        publicationState = PublicationState.CURRENT.getValue();
        this.location = location;
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

    public int getPublicationState() {
        return publicationState;
    }

    public String getLocation() {
        return location;
    }

    public void terminatePublication() {
        publicationState = PublicationState.TERMINATED.getValue();
    }
}
