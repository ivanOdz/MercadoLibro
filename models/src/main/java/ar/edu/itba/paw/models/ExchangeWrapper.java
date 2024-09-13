package ar.edu.itba.paw.models;

import java.util.List;

public class ExchangeWrapper {

    private final Exchange exchange;
    private final String requesterLocation, requesterMail, requesterUsername;
    private final Book offererBook, requesterBook;
    private final BookModel offererBookModel, requesterBookModel;
    private final List<BookImage> requesterBookImages, offererBookImages;
    private final List<String> requesterBookAuthor, offererBookAuthor;

    public ExchangeWrapper(Exchange exchange, String requesterLocation, String requesterMail, String requesterUsername, Book offererBook, Book requesterBook, BookModel offererBookModel, BookModel requesterBookModel, List<BookImage> requesterBookImages, List<BookImage> offererBookImages, List<String> requesterBookAuthor, List<String> offererBookAuthor) {
        this.exchange = exchange;
        this.requesterLocation = requesterLocation;
        this.requesterMail = requesterMail;
        this.requesterUsername = requesterUsername;
        this.offererBook = offererBook;
        this.requesterBook = requesterBook;
        this.offererBookModel = offererBookModel;
        this.requesterBookModel = requesterBookModel;
        this.requesterBookImages = requesterBookImages;
        this.offererBookImages = offererBookImages;
        this.requesterBookAuthor = requesterBookAuthor;
        this.offererBookAuthor = offererBookAuthor;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public String getRequesterLocation() {
        return requesterLocation;
    }

    public String getRequesterMail() {
        return requesterMail;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public Book getOffererBook() {
        return offererBook;
    }

    public Book getRequesterBook() {
        return requesterBook;
    }

    public BookModel getOffererBookModel() {
        return offererBookModel;
    }

    public BookModel getRequesterBookModel() {
        return requesterBookModel;
    }

    public List<BookImage> getRequesterBookImages() {
        return requesterBookImages;
    }

    public List<BookImage> getOffererBookImages() {
        return offererBookImages;
    }

    public List<String> getRequesterBookAuthor() {
        return requesterBookAuthor;
    }

    public List<String> getOffererBookAuthor() {
        return offererBookAuthor;
    }



}
