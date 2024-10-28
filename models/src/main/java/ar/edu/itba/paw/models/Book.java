package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.BookState;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "book_bookid_seq")
	@SequenceGenerator(sequenceName = "book_bookid_seq", name = "book_bookid_seq", allocationSize = 1)
	@Column(name = "bookid")
	private Long bookId;

	
	//fetch = FetchType.LAZY es para indicar cuando trae los datos -> esto esta de ejemplo nomas, LAZY es el valor default
	@ManyToOne(optional = false,  fetch = FetchType.EAGER)
	@JoinColumn(name = "ownerid", referencedColumnName = "userid")
	private User owner;

	@ManyToOne(optional = false)
	@JoinColumn(name = "bookmodelid")
	private BookModel bookModel;

	@Enumerated(EnumType.STRING)
	private BookState bookState;
	
	private int exchangesQty;

	@Column(name = "available")
	private Boolean available;

	@ManyToMany
	@JoinTable(
			name = "book_image",
			joinColumns = @JoinColumn(name = "bookid"),
			inverseJoinColumns = @JoinColumn(name = "imageid"))
	private List<Image> images;


	public Book(){
		// only for JPA
	}

    public Book(Long bookId, User owner, BookModel bookModel, BookState bookState, int exchangesQty, boolean available, List<Image> images) {
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

	public List<Image> getImages() {
		return images;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setBookModel(BookModel bookModel) {
		this.bookModel = bookModel;
	}

	public void setBookState(BookState bookState) {
		this.bookState = bookState;
	}

	public void setExchangesQty(int exchangesQty) {
		this.exchangesQty = exchangesQty;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}
