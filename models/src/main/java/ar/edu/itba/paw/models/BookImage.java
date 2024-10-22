package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "book_image")
public class BookImage {

    @Id
    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private final Long bookId;

    @Id
    @ManyToOne
    @JoinColumn(name = "imageid", nullable = false)
    private final Integer imageId;

    private final Timestamp imageDatetime;

    public BookImage(Long bookId, int imageOrder, Integer imageId, Timestamp imageDatetime) {
        this.bookId = bookId;
        this.imageOrder = imageOrder;
        this.imageId = imageId;
        this.imageDatetime = imageDatetime;
    }

    private final int imageOrder;

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
