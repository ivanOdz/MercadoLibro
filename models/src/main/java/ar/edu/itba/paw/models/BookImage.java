package ar.edu.itba.paw.models;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "book_image")
public class BookImage implements Serializable {

    @Id
//    @ManyToOne
    @Column(name = "bookid", nullable = false)
    private Long bookId;

    @Column(name = "imageorder")
    private Integer imageOrder;

    @Id
//    @ManyToOne
    @Column(name = "imageid", nullable = false)
    private Long imageId;

    @Column(name = "imagedatetime")
    private Timestamp imageDatetime;

    public BookImage(Long bookId, Integer imageOrder, Long imageId, Timestamp imageDatetime) {
        this.bookId = bookId;
        this.imageOrder = imageOrder;
        this.imageId = imageId;
        this.imageDatetime = imageDatetime;
    }


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
