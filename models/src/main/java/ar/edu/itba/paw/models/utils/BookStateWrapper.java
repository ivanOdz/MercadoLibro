package ar.edu.itba.paw.models.utils;

public class BookStateWrapper {

	private final BookState bookState;
	private String displayName;
	private int resultByState;

	// Constructor que inicializa resultByState con 0
	public BookStateWrapper(BookState bookState, String displayName) {
		this.bookState = bookState;
		this.displayName = displayName;
	}

	// Constructor que acepta el valor de resultByState
	public BookStateWrapper(BookState bookState, int resultByState) {
		this.bookState = bookState;
		this.resultByState = resultByState;
	}

	public BookStateWrapper(BookState bookState,  String displayName, int resultByState) {
		this.bookState = bookState;
		this.displayName = displayName;
		this.resultByState = resultByState;
	}

	public BookState getBookState() {
		return bookState;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getResultByState() {
		return resultByState;
	}
}
