package ar.edu.itba.paw.models.utils;

public class BookStateWrapper {

	private final BookState bookState;
	private final int resultByState;

	public BookStateWrapper(BookState bookState, int resultByState) {
		this.bookState = bookState;
		this.resultByState = resultByState;
	}

	public BookState getBookState() {
		return bookState;
	}

	public int getResultByState() {
		return resultByState;
	}
}
