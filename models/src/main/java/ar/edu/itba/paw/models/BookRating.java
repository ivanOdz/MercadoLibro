package ar.edu.itba.paw.models;


import javax.persistence.*;

@Entity
@Table(name = "book_rating")
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "book_rating_ratingId_seq")
    @SequenceGenerator(sequenceName = "book_rating_ratingId_seq", name = "book_rating_ratingId_seq", allocationSize = 1)
    @Column(name = "ratingid")
    private Long ratingId;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "bookmodelid")
    private Long bookModelId;

    @Column(name = "rating")
    private Integer rating;

    public BookRating() {
        // Hibernate
    }

    public BookRating(Long userId, Long bookModelId, Integer rating) {
        this.userId = userId;
        this.bookModelId = bookModelId;
        this.rating = rating;
    }

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookModelId() {
        return bookModelId;
    }

    public void setBookModelId(Long bookModelId) {
        this.bookModelId = bookModelId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
