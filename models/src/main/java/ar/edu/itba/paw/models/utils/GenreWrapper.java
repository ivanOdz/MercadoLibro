package ar.edu.itba.paw.models.utils;


import java.util.HashMap;

public class GenreWrapper {

	private final Genre genre;
	private String displayName;
	private int resultByGenre;


	public GenreWrapper(Genre genre, String displayName) {
		this.genre = genre;
		this.displayName = displayName;
		this.resultByGenre = 0;
	}

	public GenreWrapper(Genre genre, int resultByGenre) {
		this.genre = genre;
		this.resultByGenre = resultByGenre;
	}

	public GenreWrapper(Genre genre, String displayName, int resultByGenre) {
		this.genre = genre;
		this.displayName = displayName;
		this.resultByGenre = resultByGenre;
	}

	public int getResultByGenre() {
		return resultByGenre;
	}

	public Genre getGenre() {
		
		return genre;
	}
	
	public String getDisplayName() {
		
		return displayName;
	}
}