package ar.edu.itba.paw.webapp.dto.input;

public class ReviewInputDTO {
    private String description;
    private int rating;

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
