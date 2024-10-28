package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.Rating;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book_model")
public class BookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "book_bookmodelid_seq")
    @SequenceGenerator(sequenceName = "book_bookmodelid_seq", name = "book_bookmodelid_seq", allocationSize = 1)
    @Column(name = "bookmodelid")
    private Long bookModelId;

    private String isbn;
    private String title;
    private String editorial;
    private String description;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private int edition;
    private int weight;
    private int pages;

    @Enumerated(EnumType.STRING)
    private Language bookLanguage;

    @Enumerated(EnumType.STRING)
    private BookDimension dimension;

    @Column(name = "publicationyear")
    private short publicationYear;

    @Column(name = "ispocketedition")
    private boolean isPocketEdition;

    @Column(name = "ishardcover")
    private boolean isHardcover;

    @Column(name = "imageid")
    private Long imageId;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "bookmodelid"),
            inverseJoinColumns = @JoinColumn(name = "authorid"))
    private List<Author> authors;

    /*@Formula("SELECT AVG(rating) FROM book_rating WHERE bookmodelid = bookmodelid")
    private Double averageRating;

    @Formula("SELECT COUNT(rating) FROM book_rating WHERE bookmodelid = bookmodelid")
    private Integer ratingCount;*/

    public BookModel(){
        // only for JPA
    }

    public BookModel(Long bookModelId, String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language bookLanguage,
                     BookDimension dimension, short publicationYear, boolean isPocketEdition, boolean isHardcover, List<Author> authors, Long imageId, Rating rating) {
        this.bookModelId = bookModelId;
        this.isbn = isbn;
        this.title = title;
        this.editorial = editorial;
        this.description = description;
        this.genre = genre;
        this.edition = edition;
        this.weight = weight;
        this.pages = pages;
        this.bookLanguage = bookLanguage;
        this.dimension = dimension;
        this.publicationYear = publicationYear;
        this.isPocketEdition = isPocketEdition;
        this.isHardcover = isHardcover;
        this.authors = authors;
        this.imageId = imageId;
        // this.averageRating = rating.getRating();
       // this.ratingCount = rating.getRatingCount();
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Long getImageId() {
        return imageId;
    }

    public Long getBookModelId() {
        return bookModelId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getDescription() {
        return description;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getEdition() {
        return edition;
    }

    public int getWeight() {
        return weight;
    }

    public int getPages() {
        return pages;
    }

    public Language getBookLanguage() {
        return bookLanguage;
    }

    public BookDimension getDimension() {
        return dimension;
    }

    public short getPublicationYear() {
        return publicationYear;
    }

    public boolean isPocketEdition() {
        return isPocketEdition;
    }

    public boolean isHardcover() {
        return isHardcover;
    }

    public void setBookModelId(Long bookModelId) {
        this.bookModelId = bookModelId;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setBookLanguage(Language bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public void setDimension(BookDimension dimension) {
        this.dimension = dimension;
    }

    public void setPublicationYear(short publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setPocketEdition(boolean pocketEdition) {
        isPocketEdition = pocketEdition;
    }

    public void setHardcover(boolean hardcover) {
        isHardcover = hardcover;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
/*
    public void setRating(Rating rating) {
        this.averageRating = rating.getRating();
        this.ratingCount = rating.getRatingCount();
    }

    public Rating getRating() {
        return new Rating(averageRating != null ? averageRating : 0.0, ratingCount != null ? ratingCount : 0);
    }*/
}
