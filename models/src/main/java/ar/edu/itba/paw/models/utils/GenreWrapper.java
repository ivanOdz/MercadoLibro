package ar.edu.itba.paw.models.utils;


import java.util.HashMap;

public class GenreWrapper {

	private final Genre genre;
	private final String displayName;
	private final int resultByGenre;


	public GenreWrapper(Genre genre, String displayName) {
		this.genre = genre;
		this.displayName = displayName;
		this.resultByGenre = 0;
	}

	public GenreWrapper(Genre genre, String displayName, int resultByGenre) {
		this.genre = genre;
		this.displayName = displayName;
		this.resultByGenre = resultByGenre;
	}


	public Genre getGenre() {
		
		return genre;
	}
	
	public String getDisplayName() {
		
		return displayName;
	}
}