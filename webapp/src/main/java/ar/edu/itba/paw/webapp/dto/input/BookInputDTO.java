package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class BookInputDTO {
    private String condition;
    private URI bookModelURN;
    private URI userURN;
    private Integer rating;
    private List<URI> imageURNS;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public URI getBookModelURN() {
        return bookModelURN;
    }

    public void setBookModelURN(URI bookModelURN) {
        this.bookModelURN = bookModelURN;
    }

    public URI getUserURN() {
        return userURN;
    }

    public void setUserURN(URI userURN) {
        this.userURN = userURN;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<URI> getImageURNS() {
        return imageURNS;
    }

    public void setImageURNS(List<URI> imageURNS) {
        this.imageURNS = imageURNS;
    }

    public Long getBookModelId() {
        return UrnResolverUtil.getBookModelId(bookModelURN);
    }

    public Long getUserId() {
        return UrnResolverUtil.getUserId(userURN);
    }

    public List<Long> getImageIds() {
        return imageURNS.stream()
                .map(UrnResolverUtil::getImageId)
                .collect(Collectors.toList());
    }
}
