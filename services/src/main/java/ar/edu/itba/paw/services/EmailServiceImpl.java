package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.EmailService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
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

    public EmailServiceImpl(final JavaMailSender javaMailSender, final SpringTemplateEngine templateResolver){
        this.mailSender = javaMailSender;
        this.templateEngine = templateResolver;
    }

    @Async
    @Override
    public void sendEmail(final String receiver, Map<String, Object> variables, String templatePath, String subject) {
            MimeMessage message = mailSender.createMimeMessage();
            Locale locale =  LocaleContextHolder.getLocale();

            Context context = new Context(locale);
            context.setVariables(variables);

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                String html = templateEngine.process(templatePath, context);

                helper.setTo(receiver);
                helper.setSubject(subject);
                helper.setText(html, true);
                helper.setFrom("paw@mail.com");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            mailSender.send(message);
    }
}