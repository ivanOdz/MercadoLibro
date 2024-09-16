package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.utils.BookDimension;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

public class ModelBookForm {
    @NotBlank
    @Pattern(regexp = "^(97[89])\\d{1,5}\\d{1,7}\\d{1,7}\\d$")
    private String isbn;

    @Size(min = 1, max = 255)
    private String title;

    @NotEmpty
    private List<String> authors = new ArrayList<>();

    @NotBlank
    @Size(min = 1, max = 100)
    private String editorial;

    @Size(min = 10, max = 2000)
    private String description;

    @NotNull
    private Genre genre;

    @Min(1)
    @Max(99)
    private int edition =1;

    private Short publicationYear;

    private boolean isHardcover;

    private boolean isPocketEdition;

    @NotNull
    private BookDimension dimension;

    @NotNull
    private Language language;

    private int pages = 300;

    private int weight = 300;

    // Getters

    public @NotBlank @Pattern(regexp = "^(97[89])\\d{1,5}\\d{1,7}\\d{1,7}\\d$") String getIsbn() {
        return isbn;
    }

    public @NotBlank @Size(min = 1, max = 255) String getTitle() {
        return title;
    }

    public @NotEmpty List<String> getAuthors() {
        return authors;
    }

    public @NotBlank @Size(min = 1, max = 100) String getEditorial() {
        return editorial;
    }

    public @Size(min = 10, max = 2000) String getDescription() {
        return description;
    }

    @NotNull
    public Genre getGenre() {
        return genre;
    }

    @Min(1)
    @Max(99)
    public int getEdition() {
        return edition;
    }

    public Short getPublicationYear() {
        return publicationYear;
    }

    public boolean getIsHardcover() {
        return isHardcover;
    }

    public boolean getIsPocketEdition() {
        return isPocketEdition;
    }


    public BookDimension getDimension() {
        return dimension;
    }

    public Language getLanguage() {
        return language;
    }

    public int getPages() {
        return pages;
    }

    public int getWeight() {
        return weight;
    }

    // Setters


    public void setIsbn(@NotBlank @Pattern(regexp = "^(97[89])\\d{1,5}\\d{1,7}\\d{1,7}\\d$") String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(@NotBlank @Size(min = 1, max = 255) String title) {
        this.title = title;
    }

    public void setAuthors(@NotEmpty List<String> authors) {
        this.authors = authors;
    }

    public void setEditorial(@NotBlank @Size(min = 1, max = 100) String editorial) {
        this.editorial = editorial;
    }

    public void setDescription(@Size(min = 10, max = 2000) String description) {
        this.description = description;
    }

    public void setGenre(@NotNull Genre genre) {
        this.genre = genre;
    }

    public void setEdition(@Min(1) @Max(99) int edition) {
        this.edition = edition;
    }

    public void setPublicationYear(Short publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setHardcover(boolean hardcover) {
        isHardcover = hardcover;
    }

    public void setPocketEdition(boolean pocketEdition) {
        isPocketEdition = pocketEdition;
    }

    public void setDimension(@NotNull BookDimension dimension) {
        this.dimension = dimension;
    }

    public void setLanguage(@NotNull Language language) {
        this.language = language;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
