package ar.edu.itba.paw.webapp.dto.input;

import java.net.URI;

public class BookInputDTO {
    private String condition;
    private URI bookModelUrn;
    private URI userUrn;

    public URI getUserUrn() {
        return userUrn;
    }

    public void setUserUrn(URI userUrn) {
        this.userUrn = userUrn;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setBookModelUrn(URI bookModelUrn) {
        this.bookModelUrn = bookModelUrn;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    private Integer rating;

    public String getCondition() {
        return condition;
    }

    public URI getBookModelUrn() {
        return bookModelUrn;
    }

    public Integer getRating() {
        return rating;
    }
}
