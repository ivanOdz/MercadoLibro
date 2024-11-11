package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.utils.BookState;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

public class BookDetailsForm {

    @NotNull
    private BookState bookState;

    @Range(min = 1, max = 5)
    private int rating;

    private List<MultipartFile> imageFiles;

    private int bookCover;

    private boolean publish = false;

    private long locationId;

    // Getters

    @NotNull
    public BookState getBookState() {
        return bookState;
    }

    @Range(min = 1, max = 5)
    public int getRating() {
        return rating;
    }

    public List<MultipartFile> getImageFiles() {
        return imageFiles;
    }

    public int getBookCover() {
        return bookCover;
    }

    public boolean isPublish() {
        return publish;
    }

    public long getLocationId() {
        return locationId;
    }

    // Setters

    public void setBookState(@NotNull BookState bookState) {
        this.bookState = bookState;
    }

    public void setRating(@Range(min = 1, max = 5) int rating) {
        this.rating = rating;
    }


    public void setImageFiles(List<MultipartFile> imageFiles) {
        this.imageFiles = imageFiles;
    }

    public void setBookCover(int bookCover) {
        this.bookCover = bookCover;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
}
