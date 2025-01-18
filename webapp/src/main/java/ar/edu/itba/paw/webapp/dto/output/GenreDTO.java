package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.utils.GenreWrapper;

import javax.ws.rs.core.UriInfo;

public class GenreDTO {
    private String genre;
    private Integer amount;

    public GenreDTO() {
    }

    static public GenreDTO fromGenreWrapper(UriInfo uriInfo, GenreWrapper genre) {
        GenreDTO dto = new GenreDTO();
        dto.genre = genre.getGenre().getValue();
        dto.amount = genre.getResultByGenre();
        return dto;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
