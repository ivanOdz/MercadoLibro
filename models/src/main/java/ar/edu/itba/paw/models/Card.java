package ar.edu.itba.paw.models;


import java.util.List;
import java.util.stream.Collectors;

public class Card {
    private Publication publication;
    private Book book;
    private Image image;
    private List<Author> bookAuthors;

    public Card(Publication publication, Book book, Image image, List<Author> bookAuthors) {
        this.publication = publication;
        this.book = book;
        this.image = image;
        this.bookAuthors = bookAuthors;
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

    public List<Author> getBookAuthors() {
        return bookAuthors;
    }

    public String getAuthorsString() {
        List<String> authorNames = bookAuthors.stream()
                .map(Author::getAuthorName)
                .limit(3) // Limita a los primeros 3 autores
                .collect(Collectors.toList());

        String authorsString = String.join(", ", authorNames);

        if (bookAuthors.size() > 3) {
            authorsString += ", ...";
        }
        return authorsString;
    }
}