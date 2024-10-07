package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl(final JavaMailSender javaMailSender, final SpringTemplateEngine templateResolver) {
        this.mailSender = javaMailSender;
        this.templateEngine = templateResolver;
    }

    @Async
    @Override
    public void sendEmail(final String receiver, Map<String, Object> variables, String templatePath, String subject, String locale) {
            MimeMessage message = mailSender.createMimeMessage();
            Locale l = Locale.forLanguageTag(locale); // TODO: asegurarse q esto funcione cuando se configure el locale

            Context context = new Context(l);
            context.setVariables(variables);

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                String html = templateEngine.process(templatePath, context);

                helper.setTo(receiver);
                helper.setSubject(subject);
                helper.setText(html, true);
                helper.setFrom("mercado.libro.staff@gmail.com");
            } catch (MessagingException e) {
                LOGGER.error("Error sending email to {}. Stack strace: {} ",receiver, e.getStackTrace());
            }
            mailSender.send(message);
    }


    @Async
    @Override
        public void sendExchangeEmail(final String receiver, Map<String, Object> variables, boolean state, String locale) {
         if(!state) {
            sendEmail(receiver, variables, "exchangeRejected", "Book Exchange Rejected", locale);
        }
        else sendEmail(receiver, variables, "exchangeAccepted", "Book Exchange Accepted", locale);
    }
}