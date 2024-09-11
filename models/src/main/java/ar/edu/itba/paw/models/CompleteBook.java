package ar.edu.itba.paw.models;

public class CompleteBook {
    private final Book book;
    private final BookModel bookModel;
    private Long selectedBookId; // Campo adicional para almacenar el ID seleccionado

    public CompleteBook(Book book, BookModel bookModel) {
        this.book = book;
        this.bookModel = bookModel;
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
}
