package ar.edu.itba.paw.models;

public class CompleteBook {
    private Book book;
    private BookModel bookModel;
    private Long selectedBookId; // Campo adicional para almacenar el ID seleccionado
    private String location;

    public void setLocation(String location) {
        this.location = location;
    }

    public CompleteBook(Book book, BookModel bookModel) {
        this.book = book;
        this.bookModel = bookModel;
    }

    public String getLocation() {
        return location;
    }

    public Book getBook() {
        return book;
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public Long getSelectedBookId() {
        return selectedBookId;
    }

    public void setSelectedBookId(Long selectedBookId) {
        this.selectedBookId = selectedBookId;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setBookModel(BookModel bookModel) {
        this.bookModel = bookModel;
    }
}
