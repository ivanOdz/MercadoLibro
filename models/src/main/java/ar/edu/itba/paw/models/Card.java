package ar.edu.itba.paw.models;


import java.util.List;
import java.util.stream.Collectors;

public class Card {
    private Publication publication;
    private Book book;
    private BookModel bookModel;
    private List<BookImage> bookImages;
    private List<Author> bookAuthors;
    private String location;

    public Card(Publication publication, Book book, BookModel bookModel,List<BookImage> images, List<Author> bookAuthors, String location) {
        this.publication = publication;
        this.book = book;
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

    public List<BookImage> getImages() {
        return bookImages;
    }

    public String getLocation() {
        return location;
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

    public long getUserId(){
        return publication.getUserId();
    }
}