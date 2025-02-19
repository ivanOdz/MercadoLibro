package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.User;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface EmailService {
    @Async
    void sendExchangeEmail(User requester, User offerer, Book bookRequested, Book bookOffered, boolean state);

    @Async
    void sendExchangeRequestEmail(User requester, User offerer, Book bookRequested, Book bookOffered,long acceptCode);

    @Async
    void sendVerificationEmail(User user);

    @Async
    void sendPasswordChangeEmail(User user);

}
