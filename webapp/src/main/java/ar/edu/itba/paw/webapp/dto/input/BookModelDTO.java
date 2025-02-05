package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class BookModelDTO {
    private String isbn;
    private String title;
    private String editorial;
    private String description;
    private String genre;
    private Integer edition;
    private Integer weight;
    private Integer pages;
    private String bookLanguage;
    private String dimension;
    private Short publicationYear;
    private Boolean isPocketEdition;
    private Boolean isHardcover;
    private Integer ratingCount;
    private Double averageRating;
    private List<String> authors;
    private URI cover;
    private URI self;

    public static BookModelDTO fromBookModel(final UriInfo uriInfo, final BookModel bookModel){
        final BookModelDTO dto = new BookModelDTO();
        dto.isbn = bookModel.getIsbn();
        dto.title = bookModel.getTitle();
        dto.editorial = bookModel.getEditorial();
        dto.description = bookModel.getDescription();
        dto.genre = bookModel.getGenre().toString();
        dto.edition = bookModel.getEdition();
        dto.weight = bookModel.getWeight();
        dto.pages = bookModel.getPages();
        dto.bookLanguage = bookModel.getBookLanguage().toString();
        dto.dimension = bookModel.getDimension().toString();
        dto.publicationYear = bookModel.getPublicationYear();
        dto.isPocketEdition = bookModel.getPocketEdition();
        dto.isHardcover = bookModel.getHardcover();
        dto.ratingCount = bookModel.getRatingCount();
        dto.averageRating = bookModel.getAverageRating();

        dto.self = URI.create("/book_models/" + bookModel.getBookModelId());

        dto.authors = bookModel.getAuthors().stream().map(Author::getAuthorName).toList();
        dto.cover = URI.create("/images/" + bookModel.getImage().getImageId());
        return dto;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
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

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
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

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public Long getCoverId() {
        return UrnResolverUtil.getImageId(cover);
    }

    public void setCover(URI cover) {
        this.cover = cover;
    }

    public URI getCover() {
        return cover;
    }
}
