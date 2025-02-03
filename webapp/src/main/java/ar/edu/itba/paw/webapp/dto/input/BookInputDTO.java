package ar.edu.itba.paw.webapp.dto.input;

import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

import java.net.URI;
import java.util.List;

public class BookInputDTO {
    private String condition;
    private Long bookModelId;
    private Long userId;
    private List<Long> imageIds;

    public List<Long> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<URI> imageIds) {
        this.imageIds = imageIds.stream().map(UrnResolverUtil::getImageId).toList();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(URI userId) {
        this.userId = UrnResolverUtil.getUserId(userId);
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setBookModelId(URI bookModelId) {
        this.bookModelId = UrnResolverUtil.getBookModelId(bookModelId);
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    private Integer rating;

    public String getCondition() {
        return condition;
    }

    public Long getBookModelId() {
        return bookModelId;
    }

    public Integer getRating() {
        return rating;
    }

}
