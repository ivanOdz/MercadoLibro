package ar.edu.itba.paw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;


    public void sendEmail(final String receiver, Map<String, Object> variables, String templatePath) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            Context context = new Context();
            context.setVariables(variables);

            String html = templateEngine.process(templatePath, context);

            try {
                helper.setTo(receiver);
                helper.setSubject("Test email");
                helper.setText(html, true);
                helper.setFrom("jtechenski@gmail.com");
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            mailSender.send(message);
    }
}