package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class BookInputDTO {
    private String condition;
    private URI bookModel;
    private URI user;
    private Integer rating;
    private List<URI> imageURNS;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public URI getBookModelURN() {
        return bookModel;
    }

    public void setBookModelURN(URI bookModelURN) {
        this.bookModel = bookModelURN;
    }

    public URI getUserURN() {
        return user;
    }

    public void setUserURN(URI userURN) {
        this.user = userURN;
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
        return UrnResolverUtil.getBookModelId(bookModel);
    }

    public Long getUserId() {
        return UrnResolverUtil.getUserId(user);
    }

    public List<Long> getImageIds() {
        return imageURNS.stream()
                .map(UrnResolverUtil::getImageId)
                .collect(Collectors.toList());
    }

    public URI getBookModel() {
        return bookModel;
    }

    public void setBookModel(URI bookModel) {
        this.bookModel = bookModel;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }
}
