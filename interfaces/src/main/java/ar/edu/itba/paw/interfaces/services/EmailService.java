package ar.edu.itba.paw.interfaces.services;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmail(final String receiver);
}
