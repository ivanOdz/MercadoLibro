package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.PublicationState;

import java.util.List;

public class Book {

    private final long bookId;
    private final String isbn;
    private final String title;
    private final List<String> author;
    private final String editorial;
    private final String description;
    private final Genres genre;
    private final PublicationState publicationState;
    private final int edition;
    private final int rating;
    private final long image;
    private final long userId;

    public Book(long bookId, String isbn, String title, List<String> author, String editorial, String description, Genres genre, PublicationState publicationState, int edition, int rating, long image, long userId) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.editorial = editorial;
        this.description = description;
        this.genre = genre;
        this.publicationState = publicationState;
        this.edition = edition;
        this.rating = rating;
        this.image = image;
        this.userId = userId;
    }

    public long getBookId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthor() {
        return author;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getDescription() {
        return description;
    }

    public Genres getGenre() {
        return genre;
    }

    public PublicationState getPublicationState() {
        return publicationState;
    }

    public int getEdition() {
        return edition;
    }

    public int getRating() {
        return rating;
    }

    public long getImage() {
        return image;
    }

    public long getUserId() {
        return userId;
    }
}