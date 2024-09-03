package ar.edu.itba.paw.models;

import java.util.List;

public class Book {

    private final long bookId;
    private final String isbn;
    private final String title;
    private final List<String> authors;
    private final String editorial;
    private final String description;
    private final int genre;
    private int publicationState;
    private final int edition;
    private final int rating;
    private final long image;
    private final long userId;

    public Book(long bookId, String isbn, String title, List<String> authors, String editorial, String description, int genre, int publicationState, int edition, int rating, long image, long userId) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
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

    public List<String> getAuthors() {
        return authors;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getDescription() {
        return description;
    }

    public int getGenre() {
        return genre;
    }

    public int getPublicationState() {
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