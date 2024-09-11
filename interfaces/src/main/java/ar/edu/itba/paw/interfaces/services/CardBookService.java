package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.utils.CardBook;

import java.util.List;

public interface CardBookService {

    List<CardBook> buildCardBookList(List<Book> bookList);
}
