package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.keys.BookImageId;

import javax.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "book_image")
public class BookImage {

    @EmbeddedId
    private BookImageId bookImageId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "bookId",
            referencedColumnName = "bookId",
            nullable = false)
    private Book book;

    @ManyToOne
    @MapsId("imageId")
    @JoinColumn(name = "imageId",
            nullable = false,
            referencedColumnName = "imageId")
    private Image image;

    private Integer imageOrder;
    private Timestamp imageDatetime;

    public BookImage(Book book, Integer imageOrder, Image image, Timestamp imageDatetime) {
        this.bookImageId = new BookImageId(book.getBookId(), image.getImageId());
        this.imageOrder = imageOrder;
        this.imageDatetime = imageDatetime;
        this.book = book;
        this.image = image;
    }


    public BookImage() {
        this.bookImageId = new BookImageId();
    }

    public Integer getImageOrder() {
        return imageOrder;
    }

    public Timestamp getImageDatetime() {
        return imageDatetime;
    }

    public void setBookImageId(BookImageId bookImageId) {
        this.bookImageId = bookImageId;
    }

    public void setBook(Book book) {
        this.bookImageId.setBookId(book.getBookId());
        this.book = book;
    }

    public void setImage(Image image) {
        this.bookImageId.setImageId(image.getImageId());
        this.image = image;
    }

    public void setImageOrder(Integer imageOrder) {
        this.imageOrder = imageOrder;
    }

    public void setImageDatetime(Timestamp imageDatetime) {
        this.imageDatetime = imageDatetime;
    }

    public BookImageId getBookImageId() {
        return bookImageId;
    }

    public Book getBook() {
        return book;
    }

    public Image getImage() {
        return image;
    }
}
