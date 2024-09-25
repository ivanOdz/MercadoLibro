package ar.edu.itba.paw.models;


import ar.edu.itba.paw.models.utils.PublicationState;

import java.sql.Timestamp;

public class Publication {
    private final long publicationId;
    private final Book book;
    private final PublicationState publicationState;
    private final Timestamp publicationDatetime;
    private final Location location;

    public Publication(long publicationId, Book book, PublicationState publicationState, Timestamp publicationDatetime, Location location) {
        this.publicationId = publicationId;
        this.book = book;
        this.publicationState = publicationState;
        this.publicationDatetime = publicationDatetime;
        this.location = location;
    }

    public long getPublicationId() {
        return publicationId;
    }

    public Book getBook() {
        return book;
    }

    public PublicationState getPublicationState() {
        return publicationState;
    }

    public Timestamp getPublicationDatetime() {
        return publicationDatetime;
    }

    public Location getLocation() {
        return location;
    }
}
