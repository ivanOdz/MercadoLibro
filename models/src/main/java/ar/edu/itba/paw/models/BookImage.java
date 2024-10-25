package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "book_image")
public class BookImage {

    @Id
   // @ManyToOne
   // @JoinColumn(name = "bookid", nullable = false)
    private Long bookId;

    //@Id
  //  @ManyToOne
    //@JoinColumn(name = "imageid", nullable = false)
    private Integer imageId;

    private Timestamp imageDatetime;

    public BookImage(Long bookId, int imageOrder, Integer imageId, Timestamp imageDatetime) {
        this.bookId = bookId;
        this.imageOrder = imageOrder;
        this.imageId = imageId;
        this.imageDatetime = imageDatetime;
    }

    private int imageOrder;

    public BookImage() {

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
