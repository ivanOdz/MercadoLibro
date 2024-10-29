package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "authorid_seq")
    @SequenceGenerator(sequenceName = "authorid_seq", name = "authorid_seq", allocationSize = 1)
    @Column(name = "authorid")
    private Long authorId;

    @Column(name = "authorname")
    private String authorName;

    public Author(Long authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Author() {

    }

    public Long getAuthorid() {
        return authorId;
    }

    public void setAuthorid(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
