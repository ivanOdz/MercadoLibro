package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.Rating;

import java.util.List;

public class PublicationDetail {
    private BookModel bookModel;
    private Book book;
    private List<BookImage> images;
    private Rating rating;

    public PublicationDetail(BookModel bookModel, Book book, List<BookImage> images, Rating rating) {
        this.bookModel = bookModel;
        this.book = book;
        this.images = images;
        this.rating = rating;
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public Book getBook() {
        return book;
    }

    public List<BookImage> getImages() {
        return images;
    }

    public Rating getRating() {
        return rating;
    }

    public void setBookModel(BookModel bookModel) {
        this.bookModel = bookModel;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setImages(List<BookImage> images) {
        this.images = images;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

}
