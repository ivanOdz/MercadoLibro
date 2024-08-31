package ar.edu.itba.paw.models;

public class Publication {

    private final long publicationId;
    private final long bookId;
    private final int userId;
    private final int publicationState;
    private final String location;

    public Publication(long publicationId, int bookId, int userId, int publicationState, String location) {
        this.publicationId = publicationId;
        this.bookId = bookId;
        this.userId = userId;
        this.publicationState = publicationState;
        this.location = location;
    }

    public long getPublicationId() {
        return publicationId;
    }

    public long getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPublicationState() {
        return publicationState;
    }

    public String getLocation() {
        return location;
    }

}
