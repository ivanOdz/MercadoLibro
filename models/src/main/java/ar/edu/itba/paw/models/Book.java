package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookState;

public class Book {

	private final long bookId;
	private final long bookModelId;
	private final long ownerId;
	private final BookState bookState;
	private final int exchangesQty;
	private final int rating;

	public Book(long bookId, long bookModelId, long ownerId, BookState bookState, int exchangesQty, int rating) {
		this.bookId = bookId;
		this.bookModelId = bookModelId;
		this.ownerId = ownerId;
		this.bookState = bookState;
		this.exchangesQty = exchangesQty;
		this.rating = rating;
	}

	public long getBookId() {
		return bookId;
	}

	public long getBookModelId() {
		return bookModelId;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public BookState getBookState() {
		return bookState;
	}

	public int getExchangesQty() {
		return exchangesQty;
	}

	public int getRating() {
		return rating;
	}
}
