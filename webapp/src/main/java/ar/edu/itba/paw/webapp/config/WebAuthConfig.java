package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:rememberme.key")
    private Resource rememberMeKey;
    @Autowired
    private UserDetailsService userDetails;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/")
                // recursos por roles ==> accept
                // importante orden de definicion de reglas
                .and().authorizeRequests()
                .antMatchers("/images/**, /css/**, /jsp/**").permitAll()
                .antMatchers("/", "/publications/*").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/create","/login","/mail_input", "/change_password_solicited","/change_password", "/success_registration", "/mail_input_message", "/success_password","/failed_authentication").anonymous()
                .antMatchers("/post/edit").authenticated()
                .antMatchers("/post/{postId}").access("@accessHelper.isOwner(#pricipal, #postId)") // para areas de acceso a un admin
//                    .antMatchers("/**").authenticated()
                .antMatchers("/book/new_book","/book/new_book_model/**", "/start_exchange", "/profile", "/requests", "/offers").authenticated()
                //.antMatchers("/book/edit_book").hasRole("EXPLORER")
                //.antMatchers("/publications/edit_publication").hasRole("PUBLISHER")
                .and().formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/login")
                .defaultSuccessUrl("/check_verify", false)
                .failureUrl("/login?error=true")
                .and().rememberMe()
                .rememberMeParameter("remember_me")
                .key(new String(rememberMeKey.getInputStream().readAllBytes()))
                .userDetailsService(userDetails)
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and().exceptionHandling()
                .accessDeniedPage("/403")
                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/user_auth"))
                .and().csrf().disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "favicon.ico","/403");
    }
}
