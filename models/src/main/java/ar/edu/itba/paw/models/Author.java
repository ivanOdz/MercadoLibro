package ar.edu.itba.paw.models;

public class Author {

    private final long authorId;
    private final String authorName;

    public Author(long authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }
}
