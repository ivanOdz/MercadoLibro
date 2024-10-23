package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "book_rating")
public class BookRating {

    @Column(name = "userid", nullable = false)
    private Long userId;

    @Column(name = "bookmodelid", nullable = false)
    private Long bookModelId;

    @Column(name = "rating")
    private Integer rating;

    BookRating() {
        // only for JPA
    }

    public BookRating(Long userId, Long bookModelId, Integer rating) {
        this.userId = userId;
        this.bookModelId = bookModelId;
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBookModelId() {
        return bookModelId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBookModelId(Long bookModelId) {
        this.bookModelId = bookModelId;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
