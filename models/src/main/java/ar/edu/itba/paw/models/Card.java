package ar.edu.itba.paw.models;


import java.util.List;
import java.util.stream.Collectors;

public class Card {
    private final Publication publication;
    private final Book book;
    private final BookModel bookModel;
    private final List<BookImage> bookImages;
    private final List<Author> bookAuthors;
    private final String location;

    public Card(Publication publication, Book book, BookModel bookModel, List<BookImage> bookImages, List<Author> bookAuthors, String location) {
        this.publication = publication;
        this.book = book;
        this.bookModel = bookModel;
        this.bookImages = bookImages;
        this.bookAuthors = bookAuthors;
        this.location = location;
    }


    public Publication getPublication() {
        return publication;
    }

    public Book getBook() {
        return book;
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public List<BookImage> getBookImages() {
        return bookImages;
    }

    public List<Author> getBookAuthors() {
        return bookAuthors;
    }

    public String getLocation() {
        return location;
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

    public long getUserId(){
        return publication.getUserId();
    }
}