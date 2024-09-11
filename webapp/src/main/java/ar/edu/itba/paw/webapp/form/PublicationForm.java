package ar.edu.itba.paw.webapp.form;

public class PublicationForm {

    private String location;
    private long bookId;

    public PublicationForm(long bookId, String location) {
        this.bookId = bookId;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public long getBookId() {
        return bookId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
}
