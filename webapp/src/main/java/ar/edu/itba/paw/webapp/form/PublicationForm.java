package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.*;

import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PublicationForm {

    @NotBlank
    @Pattern(regexp = "^(97[89])\\d{1,5}\\d{1,7}\\d{1,7}\\d$")
    private String isbn;

    @NotBlank
    @Size(min = 1, max = 255)
    private String title;
    
    @NotEmpty
    private List<String> authors;
    
    @NotBlank
    @Size(min = 1, max = 100)
    private String editorial;

    @Size(min = 10, max = 2000)
    private String description;

    @NotNull
    private Genre genre;
    
    @NotNull
    private BookState bookState;
    
    @Min(1)
    @Max(99)
    private int edition;

    @Min(1)
    @Max(5)
    private int rating;

    private MultipartFile imageFile;

    @NotBlank
    private String location;

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

    @NotNull
    public BookState getBookState() {
    	return bookState;
    }
    
    @Min(1)
    @Max(99)
    public int getEdition() {
        return edition;
    }

    @Min(1)
    @Max(5)
    public int getRating() {
        return rating;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public @NotBlank String getLocation() {
        return location;
    }


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
    
    public void setBookState(@NotNull BookState bookState) {
    	this.bookState = bookState;
    }
    
    public void setEdition(@Min(1) @Max(99) int edition) {
        this.edition = edition;
    }

    public void setRating(@Min(1) @Max(5) int rating) {
        this.rating = rating;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public void setLocation(@NotBlank String location) {
        this.location = location;
    }
}
