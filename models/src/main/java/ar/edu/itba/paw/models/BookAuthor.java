package ar.edu.itba.paw.models;

public class BookAuthor {

    private final long bookModelId;
    private final long authorId;

    public BookAuthor(long bookModelId, long authorId) {
        this.bookModelId = bookModelId;
        this.authorId = authorId;
    }

    public long getBookModelId() {
        return bookModelId;
    }

    public long getAuthorId() {
        return authorId;
    }

}





