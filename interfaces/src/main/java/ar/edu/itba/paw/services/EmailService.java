package ar.edu.itba.paw.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface EmailService {
    public void sendEmail(final String receiver, Map<String, Object> variables, String templatePath);
}
