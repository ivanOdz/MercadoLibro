package ar.edu.itba.paw.models.utils;


public class BookStateWrapper {
	
	private final BookState bookState;
	private final String displayName;
	
	public BookStateWrapper(BookState bookState, String displayName) {
		
		this.bookState = bookState;
		this.displayName = displayName;
	}
	
	public BookState getBookState() {
		
		return bookState;
	}
	
	public String getDisplayName() {
		
		return displayName;
	}
}