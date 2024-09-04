package ar.edu.itba.paw.models;

public class Card {
    private Publication publication;
    private Book book;
    private Image image;

    public Card(Publication publication, Book book, Image image) {
        this.publication = publication;
        this.book = book;
        this.image = image;
    }

    public Publication getPublication() {
        return publication;
    }

    public Book getBook() {
        return book;
    }

    public Image getImage() {
        return image;
    }
}
