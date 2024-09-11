package ar.edu.itba.paw.models.utils;

import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.Image;

import java.util.List;
import java.util.stream.Collectors;

public class CardBook {
    private final Book book;
    private final BookModel bookModel;
    private final Image image;
    private final List<Author> bookAuthors;

    public CardBook(Book book, BookModel bookModel, Image image, List<Author> bookAuthors) {
        this.book = book;
        this.bookModel = bookModel;
        this.image = image;
        this.bookAuthors = bookAuthors;
    }

    public Book getBook() {
        return book;
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public Image getImage() {
        return image;
    }

    public List<Author> getAuthors() {
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
