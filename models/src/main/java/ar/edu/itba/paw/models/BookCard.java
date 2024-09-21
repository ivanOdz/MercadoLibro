package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.PublicationState;


public class BookCard {
    private final long bookId;
    private final String title;
    private final Long imageId;
    private final String authors;
    private final float rating;
    private final Genre genre;
    private final BookState bookState;
    private final PublicationState publicationState;


    public BookCard(long bookId, String title, Long imageId, String authors, float rating, Genre genre, BookState bookState, PublicationState publicationState) {
        this.bookId = bookId;
        this.title = title;
        this.imageId = imageId;
        this.authors = authors;
        this.rating = rating;
        this.genre = genre;
        this.bookState = bookState;
        this.publicationState = publicationState;
    }

    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public Long getImageId() {
        return imageId;
    }

    public String getAuthors() {
        return authors;
    }

    public float getRating() {
        return rating;
    }

    public Genre getGenre() {
        return genre;
    }

    public BookState getBookState() {
        return bookState;
    }

    public PublicationState getPublicationState() {return publicationState;}
}
