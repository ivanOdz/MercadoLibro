package ar.edu.itba.paw.models.utils;

public class Rating {
    private double rating;
    private int ratingCount;

    public Rating(double rating, int ratingCount) {
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}
