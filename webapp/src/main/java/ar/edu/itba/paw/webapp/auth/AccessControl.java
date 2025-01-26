package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.BookService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;


@Component
public class AccessControl {

    @Autowired
    private PawUserDetailsService pawUserDetailsService;

    @Autowired
    private BookService bookService;

    public Boolean userAccess(HttpRequest request, Long id) {

        return true;
    }

    public Boolean booksAccess(HttpServletRequest request) {
        long userId = Long.parseLong(request.getParameter("owner"));
        return getUser().getUserId().equals(userId);
    }

    public Boolean modifyBookAccess(HttpServletRequest request, Long id) {
        long userId = getUser().getUserId();

        Book b = bookService.getBookById(id);
        return b.getOwner().getUserId().equals(userId);
    }

    private User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
