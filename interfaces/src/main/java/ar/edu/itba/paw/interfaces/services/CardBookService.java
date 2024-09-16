package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.BookModel;
import ar.edu.itba.paw.models.CardBook;

import java.util.List;

public interface CardBookService {

    List<CardBook> buildCardBookList(List<Book> bookList);

    List<CardBook> buildCardBookModelList(List<BookModel> bookList);
}
