package ar.edu.itba.paw.models.utils;


public class GenreWrapper {
	
	private Genres genre;
	private String displayName;
	
	public GenreWrapper(Genres genre, String displayName) {
		
		this.genre = genre;
		this.displayName = displayName;
	}
	
	public Genres getGenre() {
		
		return genre;
	}
	
	public String getDisplayName() {
		
		return displayName;
	}
}