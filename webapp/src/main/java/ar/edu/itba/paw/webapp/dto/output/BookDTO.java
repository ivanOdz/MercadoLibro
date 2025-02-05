package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookImage;
import ar.edu.itba.paw.webapp.utils.UrnResolverUtil;

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

        dto.self = URI.create("/books/" + book.getBookId());
        dto.owner = URI.create("/users/" + book.getOwner().getUserId());
        dto.bookModel = URI.create("/book_models/" + book.getBookModel().getBookModelId());

        dto.images = book.getImages().stream()
                .map(i -> URI.create("/images/" + i.getImage().getImageId()))
                .collect(Collectors.toList());

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
    public Long getOwnerId() {
        return UrnResolverUtil.getUserId(owner);
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
