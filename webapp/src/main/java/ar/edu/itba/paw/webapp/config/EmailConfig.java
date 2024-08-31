package ar.edu.itba.paw.webapp.config;

// https://howtodoinjava.com/spring-core/send-email-with-spring-javamailsenderimpl-example/
// https://docs.spring.io/spring-framework/reference/integration/email.html#mail-usage-mime
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Properties;

@ComponentScan({"ar.edu.itba.paw.interfaces.services"})
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


    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }


    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver
                = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
