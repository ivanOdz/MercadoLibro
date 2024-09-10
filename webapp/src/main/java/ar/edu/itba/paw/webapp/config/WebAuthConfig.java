package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
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
import org.springframework.util.ResourceUtils;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:rememberme.key")
    private Resource rememberMeKey;
    @Autowired
    private PawUserDetailsService userDetails;

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
                    .invalidSessionUrl("/login")
                // recursos por roles ==> accept
                // importante orden de definicion de reglas
                .and().authorizeRequests()
                    .antMatchers("/create","/login","/mail_input", "/change_password_solicited","/change_password", "/success_registration").anonymous()
                    .antMatchers("/post/edit").hasRole("EDITOR")
                    .antMatchers("/post/{postId}").access("@accessHelper.isOwner(#pricipal, #postId)") // para areas de acceso a un admin
                    .antMatchers("/**").authenticated()
                .and().formLogin()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginPage("/login")
                    .defaultSuccessUrl("/check_verify", false)
                .and().rememberMe()
                    .rememberMeParameter("remember_me")
//                    .key(new String(rememberMeKey.getInputStream().readAllBytes()))
                    .userDetailsService(userDetails)
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                .and().exceptionHandling()
                    .accessDeniedPage("/403")
                .and().csrf().disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "favicon.ico","/403");
    }
}
