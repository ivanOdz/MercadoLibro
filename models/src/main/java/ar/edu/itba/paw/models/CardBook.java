package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.PublicationState;

import java.util.List;
import java.util.stream.Collectors;

public class CardBook {
    private final Book book;
    private final BookModel bookModel;
    private final Long image;
    private final List<Author> bookAuthors;
    private final boolean canPublish;

    public CardBook(Book book, BookModel bookModel, Long image, List<Author> bookAuthors, boolean canPublish) {
        this.book = book;
        this.bookModel = bookModel;
        this.image = image;
        this.bookAuthors = bookAuthors;
        this.canPublish = canPublish;
    }

    public Book getBook() {
        return book;
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public Long getImage() {
        return image;
    }

    public List<Author> getBookAuthors() {
        return bookAuthors;
    }

    public boolean getCanPublish() {
        return canPublish;
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
