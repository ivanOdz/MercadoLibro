package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "authorid_seq")
    @SequenceGenerator(sequenceName = "authorid_seq", name = "authorid_seq", allocationSize = 1)
    @Column(name = "authorid")
    private Long authorid;

    private String authorName;

    public Author(Long authorid, String authorName) {
        this.authorid = authorid;
        this.authorName = authorName;
    }

    public Author() {

    }

    public Long getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Long authorid) {
        this.authorid = authorid;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
