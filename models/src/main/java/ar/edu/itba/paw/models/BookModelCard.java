package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.Genre;

public class BookModelCard {

    private final long bookModelId;
    private final Long imageId;
    private final String title;
    private final String authors;
    private final Genre genre;
    private final String publisher;
    private final String description;
    private final float rating;


    public BookModelCard(long bookModelId, Long imageId, String title, String authors, Genre genre, String publisher, String description, float rating) {
        this.bookModelId = bookModelId;
        this.imageId = imageId;
        this.title = title;
        this.authors = authors;
        this.genre = genre;
        this.publisher = publisher;
        this.description = description;
        this.rating = rating;
    }

    public long getBookModelId() {
        return bookModelId;
    }

    public Long getImageId() {
        return imageId;
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

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }
}
