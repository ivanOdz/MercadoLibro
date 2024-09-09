package ar.edu.itba.paw.models.utils;


public class GenreWrapper {
	
	private Genre genre;
	private String displayName;
	
	public GenreWrapper(Genre genre, String displayName) {
		
		this.genre = genre;
		this.displayName = displayName;
	}
	
	public Genre getGenre() {
		
		return genre;
	}
	
	public String getDisplayName() {
		
		return displayName;
	}
}