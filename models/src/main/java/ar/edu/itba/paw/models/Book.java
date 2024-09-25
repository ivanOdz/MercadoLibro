package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookState;

import java.util.List;


public class Book {
	private final long bookId;
	private final User owner;
	private final BookModel bookModel;
	private final BookState bookState;
	private final int exchangesQty;
	private final boolean available;
	private final List<Long> images;


    public Book(long bookId, User owner, BookModel bookModel, BookState bookState, int exchangesQty, boolean available, List<Long> images) {
        this.bookId = bookId;
        this.owner = owner;
        this.bookModel = bookModel;
        this.bookState = bookState;
        this.exchangesQty = exchangesQty;
        this.available = available;
        this.images = images;
    }

	public long getBookId() {
		return bookId;
	}

	public User getOwner() {
		return owner;
	}

	public BookModel getBookModel() {
		return bookModel;
	}

	public BookState getBookState() {
		return bookState;
	}

	public int getExchangesQty() {
		return exchangesQty;
	}

	public boolean isAvailable() {
		return available;
	}

	public List<Long> getImages() {
		return images;
	}
}
