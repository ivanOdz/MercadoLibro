package ar.edu.itba.paw.models.utils.keys;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookImageId implements Serializable {
    private Long bookId;
    private Long imageId;


    public BookImageId() {}

    public BookImageId(Long bookId, Long imageId) {
        this.bookId = bookId;
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookImageId that = (BookImageId) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, imageId);
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getImageId() {
        return imageId;
    }
}
