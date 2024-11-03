package ar.edu.itba.paw.models.utils;

public class GenreWrapper {

	private final Genre genre;
	private final int resultByGenre;

	public GenreWrapper(Genre genre, int resultByGenre) {
		this.genre = genre;
		this.resultByGenre = resultByGenre;
	}

	public int getResultByGenre() {
		return resultByGenre;
	}

	public Genre getGenre() {
		return genre;
	}
}