package ar.edu.itba.paw.webapp.config;

// https://howtodoinjava.com/spring-core/send-email-with-spring-javamailsenderimpl-example/
// https://docs.spring.io/spring-framework/reference/integration/email.html#mail-usage-mime
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Properties;

@EnableWebMvc
@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl ms = new JavaMailSenderImpl();
        ms.setHost("smtp.gmail.com");
        ms.setPort(587);

        ms.setUsername("jtechenski@gmail.com");
        ms.setPassword("xbfxyrpucaehnkmw");

        Properties props = ms.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return ms;
    }


}
