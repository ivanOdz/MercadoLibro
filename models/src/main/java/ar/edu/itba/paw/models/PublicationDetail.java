package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Rating;

import java.sql.Timestamp;
import java.util.List;

public class PublicationDetail {
    private final long publicationId;
    private final List<Integer> images;
    private final String title;
    private final String authors;
    private final Genre genre;
    private final Rating rating;
    private final String description;
    private final BookState bookState;
    private final String location;
    private final Timestamp publicationDatetime;
    private final String publisher;


    public PublicationDetail(long publicationId, List<Integer> images, String title, String authors, Genre genre, Rating rating, String description, BookState bookState, String location, Timestamp publicationDatetime, String publisher) {
        this.publicationId = publicationId;
        this.images = images;
        this.title = title;
        this.authors = authors;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.bookState = bookState;
        this.location = location;
        this.publicationDatetime = publicationDatetime;
        this.publisher = publisher;
    }

    public long getPublicationId() {
        return publicationId;
    }

    public List<Integer> getImages() {
        return images;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public Genre getGenre() {
        return genre;
    }

    public Rating getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public BookState getBookState() {
        return bookState;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getPublicationDatetime() {
        return publicationDatetime;
    }

    public String getPublisher() {
        return publisher;
    }
}
