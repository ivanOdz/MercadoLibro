package ar.edu.itba.paw.models;

import java.sql.Timestamp;

public class BookImage {

    private final long bookId;
    private final int imageOrder;
    private final long imageId;
    private final Timestamp imageDatetime;

    public BookImage(long bookId, int imageOrder, long imageId, Timestamp imageDatetime) {
        this.bookId = bookId;
        this.imageOrder = imageOrder;
        this.imageId = imageId;
        this.imageDatetime = imageDatetime;
    }

    public long getBookId() {
        return bookId;
    }

    public int getImageOrder() {
        return imageOrder;
    }

    public long getImageId() {
        return imageId;
    }

    public Timestamp getImageDatetime() {
        return imageDatetime;
    }
}
