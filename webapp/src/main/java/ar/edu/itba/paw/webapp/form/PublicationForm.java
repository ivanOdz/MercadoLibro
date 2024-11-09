package ar.edu.itba.paw.webapp.form;

public class PublicationForm {

    private Long locationId;
    private long bookId;

    public PublicationForm(long bookId, Long locationId) {
        this.bookId = bookId;
        this.locationId = locationId;
    }

    public PublicationForm() {
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
}
