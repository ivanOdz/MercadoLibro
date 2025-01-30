package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookImage;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookDTO {
    private String state;
    private Boolean available;
    private URI self;

    private URI owner;
    private URI bookModel;
    private List<URI> images;

    public static BookDTO fromBook(final UriInfo uriInfo, final Book book){
        final BookDTO dto = new BookDTO();
        dto.state = book.getBookState().toString();
        dto.available = book.getAvailable();

        dto.self = uriInfo.getBaseUriBuilder().path("books")
                .path(String.valueOf(book.getBookId())).build();
        dto.owner = uriInfo.getBaseUriBuilder().path("users")
                .path(String.valueOf(book.getOwner().getUserId())).build();
        dto.bookModel = uriInfo.getBaseUriBuilder().path("book_models")
                .path(String.valueOf(book.getBookModel().getBookModelId())).build();

        dto.images = book.getImages().stream().map(new Function<BookImage, URI>() {
            @Override
            public URI apply(BookImage bookImage) {
                return uriInfo.getBaseUriBuilder()
                        .path("images")
                        .path(String.valueOf(bookImage.getBookImageId())).build();
            }
        }).collect(Collectors.toList());

        return dto;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public URI getOwner() {
        return owner;
    }

    public void setOwner(URI owner) {
        this.owner = owner;
    }

    public URI getBookModel() {
        return bookModel;
    }

    public void setBookModel(URI bookModel) {
        this.bookModel = bookModel;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public List<URI> getImages() {
        return images;
    }

    public void setImages(List<URI> images) {
        this.images = images;
    }


}
