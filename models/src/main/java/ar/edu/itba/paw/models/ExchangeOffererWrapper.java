package ar.edu.itba.paw.models;

import java.util.List;

public class ExchangeOffererWrapper {

    private final Exchange exchange;
    private final String offererLocation, offererMail, offererUsername;
    private final Book offererBook, requesterBook;
    private final BookModel offererBookModel, requesterBookModel;
    private final List<BookImage> requesterBookImages, offererBookImages;
    private final List<String> requesterBookAuthor, offererBookAuthor;


    public ExchangeOffererWrapper(Exchange exchange, String offererLocation, String offererMail, String offererUsername, Book offererBook, Book requesterBook, BookModel offererBookModel, BookModel requesterBookModel, List<BookImage> requesterBookImages, List<BookImage> offererBookImages, List<String> requesterBookAuthor, List<String> offererBookAuthor) {
        this.exchange = exchange;
        this.offererLocation = offererLocation;
        this.offererMail = offererMail;
        this.offererUsername = offererUsername;
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

    public String getOffererLocation() {
        return offererLocation;
    }

    public String getOffererMail() {
        return offererMail;
    }

    public String getOffererUsername() {
        return offererUsername;
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
