package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.utils.BookState;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class BookDetailsForm {

    @NotNull
    private BookState bookState;

    @Min(1)
    @Max(5)
    private int rating;

    private List<MultipartFile> imageFiles;

    @NotNull
    public BookState getBookState() {
        return bookState;
    }

    @Min(1)
    @Max(5)
    public int getRating() {
        return rating;
    }

    public List<MultipartFile> getImageFiles() {
        return imageFiles;
    }

    public void setBookState(@NotNull BookState bookState) {
        this.bookState = bookState;
    }

    public void setRating(@Min(1) @Max(5) int rating) {
        this.rating = rating;
    }

    public void setImageFiles(List<MultipartFile> imageFiles) {
        this.imageFiles = imageFiles;
    }

}
