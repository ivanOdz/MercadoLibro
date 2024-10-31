package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.util.List;

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
    private List<Author> authors;

    @Formula("(SELECT ROUND(AVG(br.rating), 1) FROM book_rating br WHERE br.bookmodelid = bookmodelid)")
    private Double averageRating;

    @Formula("(SELECT COUNT(br.rating) FROM book_rating br WHERE br.bookmodelid = bookmodelid)")
    private Integer ratingCount;

    public BookModel(){
        // only for JPA
    }

    public BookModel(Long bookModelId, String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language bookLanguage,
                     BookDimension dimension, short publicationYear, boolean isPocketEdition, boolean isHardcover, List<Author> authors, Image image) {
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

    public List<Author> getAuthors() {
        return authors;
    }

    public Image getImage() {
        return image;
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

    public void setImage(Image image) {
        this.image = image;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    public Double getAverageRating() {
        return averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }


    public boolean getIsImageNull() {
        return image == null;
    }
}
