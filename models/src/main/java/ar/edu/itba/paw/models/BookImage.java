package ar.edu.itba.paw.models;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "book_image")
public class BookImage implements Serializable {

    @Id
//    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private Long bookId;

    @Id
//    @ManyToOne
    @JoinColumn(name = "imageid", nullable = false)
    private Long imageId;

    private Timestamp imageDatetime;

    public BookImage(Long bookId, Integer imageOrder, Long imageId, Timestamp imageDatetime) {
        this.bookId = bookId;
        this.imageOrder = imageOrder;
        this.imageId = imageId;
        this.imageDatetime = imageDatetime;
    }

    private Integer imageOrder;

    public BookImage() {

    }

    public Long getBookId() {
        return bookId;
    }

    public Integer getImageOrder() {
        return imageOrder;
    }

    public Long getImageId() {
        return imageId;
    }

    public Timestamp getImageDatetime() {
        return imageDatetime;
    }
}
