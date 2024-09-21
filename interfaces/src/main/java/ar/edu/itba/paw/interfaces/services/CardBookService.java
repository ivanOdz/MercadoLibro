package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.BookCard;

import java.util.List;

public interface CardBookService {

    List<BookCard> buildCardBookList(List<Book> bookList);

    List<BookCard> buildCardBookModelList(List<BookModel> bookList);
}
