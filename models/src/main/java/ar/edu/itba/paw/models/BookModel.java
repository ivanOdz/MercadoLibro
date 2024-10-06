package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.Genre;
import ar.edu.itba.paw.models.utils.Language;
import ar.edu.itba.paw.models.utils.Rating;

public class BookModel {

    private final long bookModelId;
    private final String isbn;
    private final String title;
    private final String editorial;
    private final String description;
    private final Genre genre;
    private final int edition;
    private final int weight;
    private final int pages;
    private final Language bookLanguage;
    private final int dimension;
    private final short publicationYear;
    private final boolean isPocketEdition;
    private final boolean isHardcover;
    private final String authors;
    private final Long imageId;
    private final Rating rating;


    public BookModel(long bookModelId, String isbn, String title, String editorial, String description, Genre genre, int edition, int weight, int pages, Language bookLanguage, int dimension, short publicationYear, boolean isPocketEdition, boolean isHardcover, String authors, Long imageId,  Rating rating) {
        this.bookModelId = bookModelId;
        this.isbn = isbn;
        this.title = title;
        this.editorial = editorial;
        this.description = description;
        this.genre = genre;
        this.edition = edition;
        this.weight = weight;
        this.pages = pages;
        this.bookLanguage = bookLanguage;
        this.dimension = dimension;
        this.publicationYear = publicationYear;
        this.isPocketEdition = isPocketEdition;
        this.isHardcover = isHardcover;
        this.authors = authors;
        this.imageId = imageId;
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public String getAuthors() {
        return authors;
    }

    public Long getImageId() {
        return imageId;
    }

    public long getBookModelId() {
        return bookModelId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getDescription() {
        return description;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getEdition() {
        return edition;
    }

    public int getWeight() {
        return weight;
    }

    public int getPages() {
        return pages;
    }

    public Language getBookLanguage() {
        return bookLanguage;
    }

    public int getDimension() {
        return dimension;
    }

    public short getPublicationYear() {
        return publicationYear;
    }

    public boolean isPocketEdition() {
        return isPocketEdition;
    }

    public boolean isHardcover() {
        return isHardcover;
    }


}
