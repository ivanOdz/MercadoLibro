package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;

import java.sql.Timestamp;
import java.util.List;

public class PublicationCard {

    private final long publicationId;   // Publication
    private final String title;         // Book + BookModel + Users
    private final Long imageId;         // Image + BookImage
    private final String authors;   // Mostramos solo el autor principal  Author + BookAuthor
    private final float rating;     // Book
    private final Timestamp publicationDatetime; // Publication
    private final Genre genre;          // ModelBook
    private final BookState bookState;  // Book



    // Publication, BookModel, Book, Users,
    // BookAuthor, Author, BookImage, Image,

    public PublicationCard(long publicationId, String title, Long imageId, String authors, float rating, Timestamp publicationDatetime, Genre genre, BookState bookState) {
        this.publicationId = publicationId;
        this.title = title;
        this.imageId = imageId;
        this.authors = authors;
        this.rating = rating;
        this.publicationDatetime = publicationDatetime;
        this.genre = genre;
        this.bookState = bookState;
    }

    public long getPublicationId() {
        return publicationId;
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

    public Timestamp getPublicationDatetime() {
        return publicationDatetime;
    }

    public Genre getGenre() {
        return genre;
    }

    public BookState getBookState() {
        return bookState;
    }

}