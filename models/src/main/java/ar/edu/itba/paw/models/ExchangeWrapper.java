package ar.edu.itba.paw.models;

import java.util.List;

public class ExchangeWrapper {

    private final Exchange exchange;
    private final String requesterLocation, requesterMail, requesterUsername;
    private final Book offererBook, requesterBook;
    private final BookModel offererBookModel, requesterBookModel;
    private final Image requesterBookImage, offererBookImage;
    private final List<Author> requesterBookAuthor, offererBookAuthor;

    public ExchangeWrapper(Exchange exchange, String requesterLocation, String requesterMail, String requesterUsername, Book offererBook, Book requesterBook, BookModel offererBookModel, BookModel requesterBookModel, Image requesterBookImage, Image offererBookImage, List<Author> requesterBookAuthor, List<Author> offererBookAuthor) {
        this.exchange = exchange;
        this.requesterLocation = requesterLocation;
        this.requesterMail = requesterMail;
        this.requesterUsername = requesterUsername;
        this.offererBook = offererBook;
        this.requesterBook = requesterBook;
        this.offererBookModel = offererBookModel;
        this.requesterBookModel = requesterBookModel;
        this.requesterBookImage = requesterBookImage;
        this.offererBookImage = offererBookImage;
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

    public Image getRequesterBookImage() {
        return requesterBookImage;
    }

    public Image getOffererBookImage() {
        return offererBookImage;
    }

    public List<Author> getRequesterBookAuthor() {
        return requesterBookAuthor;
    }

    public List<Author> getOffererBookAuthor() {
        return offererBookAuthor;
    }
}
