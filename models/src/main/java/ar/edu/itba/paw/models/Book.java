package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.Rating;

public class Book {

	private final long bookId;
	private final long ownerId;
	private final BookModel bookModel;
	private final BookState bookState;
	private final int exchangesQty;


    public Book(long bookId, BookModel bookModel, long ownerId, BookState bookState, int exchangesQty) {
        this.bookId = bookId;
        this.bookModel = bookModel;
        this.ownerId = ownerId;
        this.bookState = bookState;
        this.exchangesQty = exchangesQty;
    }

	public long getBookId() {
		return bookId;
	}

	public BookModel getBookModel() {
		return bookModel;
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
}
