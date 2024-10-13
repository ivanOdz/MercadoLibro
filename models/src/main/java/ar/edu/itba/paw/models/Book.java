package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookState;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "users_userid_seq")
	@SequenceGenerator(sequenceName = "book_bookid_seq", name = "book_bookid_seq", allocationSize = 1)
	@Column(name = "bookid")
	private Long bookId;


	//fetch = FetchType.LAZY es para indicar cuando trae los datos -> esto esta de ejemplo nomas, LAZY es el valor default
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User owner;

	@ManyToOne(optional = false)
	private BookModel bookModel;


	// ASK: deberíamos hacerlo ORDINAL x como tenemos definidas las tablas o podemos hacerlo STRING?
	@Enumerated(EnumType.STRING)
	private BookState bookState;

	private int exchangesQty;

	private boolean available;

	private List<Integer> images;


	/* package */Book(){
		// only for JPA
	}

    public Book(Long bookId, User owner, BookModel bookModel, BookState bookState, int exchangesQty, boolean available, List<Integer> images) {
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

	public List<Integer> getImages() {
		return images;
	}
}
