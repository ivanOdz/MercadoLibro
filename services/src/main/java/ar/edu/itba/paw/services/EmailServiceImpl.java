package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private MessageSource messageSource;

    @Value("#{environment.webappUrl}")
    private String webappUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private void sendEmail(final String receiver, Map<String, Object> variables, String templatePath, String subject, String locale) {
            MimeMessage message = mailSender.createMimeMessage();
            Locale l = Locale.forLanguageTag(locale);

            Context context = new Context(l);
            context.setVariables(variables);

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                String html = templateEngine.process(templatePath, context);

                helper.setTo(receiver);
                helper.setSubject(subject);
                helper.setText(html, true);
                helper.setFrom("mercado.libro.staff@gmail.com");
                mailSender.send(message);
                LOGGER.info("Email sent correctly");
            } catch (MessagingException e) {
                LOGGER.warn("Error sending email");
            }
    }


    @Async
    @Override
    public void sendExchangeEmail(User requester, User offerer, Book bookRequested, Book bookOffered, boolean state){
        Map<String, Object> variables = new HashMap<>();
        variables.put("requestedBook", bookRequested.getBookModel().getTitle());
        variables.put("offeredBook", bookOffered.getBookModel().getTitle());
        variables.put("requesterEmail", requester.getMail());
        variables.put("requesterName", requester.getUsername());
        variables.put("offererName", offerer.getUsername());
        variables.put("offererEmail", offerer.getMail());
        variables.put("exchangeUrl", webappUrl + "/requests");
        variables.put("publicationsUrl", webappUrl);

        String locale = requester.getLanguage();

        if(!state) {
            sendEmail(requester.getMail(), variables, "exchangeRejected", messageSource.getMessage("email.subject.rejected", null, Locale.forLanguageTag(locale)),locale);
        } else {
            sendEmail(requester.getMail(), variables, "exchangeAccepted", messageSource.getMessage("email.subject.accepted", null, Locale.forLanguageTag(locale)), locale);
        }
    }

    @Override
    public void sendExchangeRequestEmail(User requester, User offerer, Book bookRequested, Book bookOffered, long acceptCode) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("requesterEmail", requester.getMail());
        variables.put("requesterName", requester.getUsername());
        variables.put("requestedPublication", bookRequested.getBookModel().getTitle());
        variables.put("offeredPublication", bookOffered.getBookModel().getTitle());
        variables.put("validationUrl", webappUrl + "/createexchange?accept_code=" + acceptCode+ "&state=true");
        variables.put("rejectionUrl", webappUrl + "/createexchange?accept_code=" + acceptCode + "&state=false");
        variables.put("exchangeUrl", webappUrl + "/offers"); // TODO: verificar el funcionamiento de esto

        sendEmail(offerer.getMail(), variables, "exchangeRequest", messageSource.getMessage("email.subject.request", null, Locale.forLanguageTag(offerer.getLanguage())), offerer.getLanguage());
    }

    @Override
    public void sendVerificationEmail(User user) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", user.getUsername());
        variables.put("validationUrl", webappUrl + "/auth/verify?verification_code=" + user.getVerificationCode());

        sendEmail(user.getMail(), variables, "verification", messageSource.getMessage("email.subject.verification", null, Locale.forLanguageTag(user.getLanguage())), user.getLanguage());
    }

    @Override
    public void sendPasswordChangeEmail(User user) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("validationUrl", webappUrl +"/auth/change-password?verification_code=" + user.getVerificationCode());

        String locale = user.getLanguage() != null ? user.getLanguage() : Locale.getDefault().getLanguage();

        sendEmail(user.getMail(), variables, "changePassword", messageSource.getMessage("email.subject.passwordChange", null, Locale.forLanguageTag(locale)), locale);
    }
}