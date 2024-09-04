package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.Genres;
import ar.edu.itba.paw.models.utils.BookState;
import ar.edu.itba.paw.models.utils.PublicationState;

import java.util.ArrayList;
import java.util.List;

public class Book {
	
	private long bookId;
	private String isbn;
	private String title;
	private String editorial;
	private String description;
	private Genres genre;
	private BookState bookState;
	private PublicationState publicationState;
	private int edition;
	private int rating;
	private long image;
	private long userId;
	
	public Book(long bookId, String isbn, String title, String editorial, String description, Genres genre, BookState bookState, PublicationState publicationState, int edition, int rating, long image, long userId) {
		
		this.bookId = bookId;
		this.isbn = isbn;
		this.title = title;
		this.editorial = editorial;
		this.description = description;
		this.genre = genre;
		this.bookState = bookState;
		this.publicationState = publicationState;
		this.edition = edition;
		this.rating = rating;
		this.image = image;
		this.userId = userId;
	}
	
	public long getBookId() {
		
		return bookId;
	}
	
	public void setBookId(long bookId) {
		
		this.bookId = bookId;
	}
	
	public String getIsbn() {
		
		return isbn;
	}

	public void setIsbn(String isbn) {
		
		this.isbn = isbn;
	}

	public String getTitle() {
		
		return title;
	}

	public void setTitle(String title) {
		
		this.title = title;
	}

	public String getEditorial() {
		
		return editorial;
	}

	public void setEditorial(String editorial) {
		
		this.editorial = editorial;
	}

	public String getDescription() {
		
		return description;
	}

	public void setDescription(String description) {
		
		this.description = description;
	}

	public Genres getGenre() {
		
		return genre;
	}
	
	public void setGenre(Genres genre) {
		
		this.genre = genre;
	}
	
	public BookState getBookState() {
		
		return bookState;
	}
	
	public void setBookState(BookState bookState) {
		
		this.bookState = bookState;
	}

	public PublicationState getPublicationState() {
		
		return publicationState;
	}

	public void setPublicationState(PublicationState publicationState) {
		
		this.publicationState = publicationState;
	}
	
	public int getEdition() {
		
		return edition;
	}

	public void setEdition(int edition) {
		
		this.edition = edition;
	}
	
	public int getRating() {
		
		return rating;
	}

	public void setRating(int rating) {
		
		this.rating = rating;
	}

	public long getImage() {
		
		return image;
	}
	
	public void setImage(long image) {
		
		this.image = image;
	}

	public long getUserId() {
		
		return userId;
	}

	public void setUserId(long userId) {
		
		this.userId = userId;
	}
}