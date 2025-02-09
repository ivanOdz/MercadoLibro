package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
//import javax.validation.constraints.Digits;
import java.util.Set;
import java.util.Set;

@Entity
@Table(name = "book_model")
public class BookModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "book_bookmodelid_seq")
    @SequenceGenerator(sequenceName = "book_bookmodelid_seq", name = "book_bookmodelid_seq", allocationSize = 1)
    @Column(name = "bookmodelid")
    private Long bookModelId;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "editorial")
    private String editorial;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "edition")
    private Integer edition;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "pages")
    private Integer pages;

    @Enumerated(EnumType.STRING)
    private Language bookLanguage;

    @Enumerated(EnumType.STRING)
    private BookDimension dimension;

    @Column(name = "publicationyear")
    private Short publicationYear;

    @Column(name = "ispocketedition")
    private Boolean isPocketEdition;

    @Column(name = "ishardcover")
    private Boolean isHardcover;

    @OneToOne
    @JoinColumn(name = "imageid")
    private Image image;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "bookmodelid"),
            inverseJoinColumns = @JoinColumn(name = "authorid"))
    private Set<Author> authors;

    @Formula("(SELECT COALESCE(ROUND(AVG(br.rating), 1), 0.0) FROM book_rating br WHERE br.bookmodelid = bookmodelid)")
    private Double averageRating;

    @Formula("(SELECT COUNT(br.rating) FROM book_rating br WHERE br.bookmodelid = bookmodelid)")
    private Integer ratingCount;

    public BookModel(){
        // only for JPA
    }

    public BookModel(Long bookModelId, String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language bookLanguage,
                     BookDimension dimension, short publicationYear, boolean isPocketEdition, boolean isHardcover, Set<Author> authors, Image image) {
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
        this.image = image;
    }

    public Long getBookModelId() {
        return bookModelId;
    }

    public void setBookModelId(Long bookModelId) {
        this.bookModelId = bookModelId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Language getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(Language bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public BookDimension getDimension() {
        return dimension;
    }

    public void setDimension(BookDimension dimension) {
        this.dimension = dimension;
    }

    public Short getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Short publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Boolean getPocketEdition() {
        return isPocketEdition;
    }

    public void setPocketEdition(Boolean pocketEdition) {
        isPocketEdition = pocketEdition;
    }

    public Boolean getHardcover() {
        return isHardcover;
    }

    public void setHardcover(Boolean hardcover) {
        isHardcover = hardcover;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }
}
