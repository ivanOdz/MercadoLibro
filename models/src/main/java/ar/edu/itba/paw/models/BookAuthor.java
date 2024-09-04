package ar.edu.itba.paw.models;

public class BookAuthor {

    private long bookId;
    private long authorId;

    public BookAuthor(long bookId, long authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public long getBookId() {
        return bookId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
}
