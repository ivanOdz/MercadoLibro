package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.*;

import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genres;

import java.util.ArrayList;
import java.util.List;

public class PublicationForm {

    @Size(min = 5, max = 100)
    private String username;

    @NotBlank
    @Size(min = 10, max = 100)
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
    private String mail;

    @NotBlank
    @Pattern(regexp = "^(97[89])\\d{1,5}\\d{1,7}\\d{1,7}\\d$")
    private String isbn;

    @NotBlank
    @Size(min = 1, max = 255)
    private String title;
    
    @NotEmpty
    private List<String> authors = new ArrayList<String>();
    
    @NotBlank
    @Size(min = 1, max = 100)
    private String editorial;

    @Size(min = 10, max = 2000)
    private String description;

    @NotNull
    private Genres genre;
    
    @NotNull
    private BookState bookState;
    
    @Min(1)
    @Max(99)
    private int edition;

    @Min(1)
    @Max(5)
    private int rating;

    @NotNull
    private long image;

    @NotBlank
    private String location;

    public @Size(min = 5, max = 100) String getUsername() {
        return username;
    }

    public @NotBlank @Size(min = 10, max = 100) @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$") String getMail() {
        return mail;
    }

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
    public Genres getGenre() {
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


    public long getImage() {
        return image;
    }

    public @NotBlank String getLocation() {
        return location;
    }

    public void setUsername(@Size(min = 5, max = 100) String username) {
        this.username = username;
    }

    public void setMail(@NotBlank @Size(min = 10, max = 100) @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$") String mail) {
        this.mail = mail;
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
    
    public void setGenre(@NotNull Genres genre) {
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

    public void setImage(@NotNull long image) {
        this.image = image;
    }

    public void setLocation(@NotBlank String location) {
        this.location = location;
    }
}
