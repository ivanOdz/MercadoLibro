package ar.edu.itba.paw.interfaces.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface EmailService {
    @Async
    void sendEmail(String receiver, Map<String, Object> variables, String templatePath, String subject, String locale);

    @Async
    void sendExchangeEmail(final String receiver, Map<String, Object> variables, boolean state, String locale);
}
