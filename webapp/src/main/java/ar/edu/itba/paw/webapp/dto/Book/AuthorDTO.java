package ar.edu.itba.paw.webapp.dto.Book;

import ar.edu.itba.paw.models.Author;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class AuthorDTO {
    private String name;

    private URI self;

    public static AuthorDTO fromAuthor(final UriInfo uriInfo, final Author author, final BookModel bookModel) {
        final AuthorDTO dto = new AuthorDTO();
        dto.self = uriInfo.getBaseUriBuilder().path("book_models")
                .path(String.valueOf(bookModel.getBookModelId()))
                .path(String.valueOf(author.getAuthorid())).build();
        dto.name = author.getAuthorName();
        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
