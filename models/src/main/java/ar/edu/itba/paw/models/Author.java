package ar.edu.itba.paw.models;

public class Author {
    private long authorId;
    private String authorName;

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

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
