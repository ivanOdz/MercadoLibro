package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.ws.rs.HttpMethod;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.web.cors.CorsConfiguration.ALL;

@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PawUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private BasicAuthTokenIssuerFilter basicAuthTokenIssuerFilter;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnauthorizedRequestHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ForbiddenRequestHandler();
    }


    /**
     * Access control methods
     */
    private static final String BOOKS_ACCESS = "@accessControl.booksAccess(request)";
    private static final String EXCHANGES_USER_ACCESS = "@accessControl.exchangeUserAccess(request)";
    private static final String EXCHANGES_ACCESS = "@accessControl.exchangeAccess(#id, #message_id)";
    private static final String PUBLICATION_ACCESS = "@accessControl.publicationAccess(#publication_id)";
    private static final String PUBLICATION_MODIFY_ACCESS = "@accessControl.publicationsModifyAccess(#publication_id)";
    private static final String PUBLICATION_FAVORITE_LIST_ACCESS = "@accessControl.publicationsFavoriteListAccess(#publication_id, #favorite_id)";
    private static final String PUBLICATION_FAVORITE_ACCESS = "@accessControl.publicationsFavoriteAccess(#request, #publication_id)";
    private static final String PUBLICATIONS_GENERAL_ACCESS = "@accessControl.publicationsGeneralAccess(request)";
    private static final String USER_ACCESS = "@accessControl.userAccess(#id)";
    private static final String REVIEW_ACCESS = "@accessControl.reviewAccess(#id, #ur_id)";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(ALL));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"));
        configuration.addAllowedHeader(ALL);
        configuration.setExposedHeaders(Arrays.asList(// "ETag", "Total-Elements",
        	    "authorization", "link", "location", "x-access-token", "x-refresh-token", "x-user-uri", "set-cookie",
        	    "x-total-count", "x-total-pages", "x-current-page",
        	    "x-genre-fiction", "x-genre-non-fiction", "x-genre-mystery", "x-genre-thriller",
        	    "x-genre-science-fiction", "x-genre-fantasy", "x-genre-romance", "x-genre-historical-fiction",
        	    "x-genre-horror", "x-genre-biography", "x-genre-autobiography", "x-genre-memoir",
        	    "x-genre-young-adult", "x-genre-childrens-literature", "x-genre-graphic-novel",
        	    "x-genre-classic", "x-genre-adventure", "x-genre-dystopian", "x-genre-self-help",
        	    "x-genre-poetry", "x-genre-literary-fiction", "x-genre-crime", "x-genre-western",
        	    "x-genre-contemporary", "x-genre-religious-spiritual", "x-genre-philosophy", "x-genre-science",
        	    "x-genre-travel", "x-genre-true-crime", "x-genre-historical-non-fiction", "x-genre-other",
        	    "x-bookstate-new", "x-bookstate-like-new", "x-bookstate-good", "x-bookstate-acceptable", "x-bookstate-worn"
        	));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http.csrf().disable().cors().and()

        // Set session management to stateless
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()

        // Set unauthorized and forbidden requests exception handler
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler())
        .and()

        // Set permissions on endpoints
        //.authorizeRequests().anyRequest().authenticated()

        .authorizeRequests()

                /*
                 * Book controller
                 **/

                .antMatchers(HttpMethod.GET,"/api/books")
                    .access(BOOKS_ACCESS)

                .antMatchers(HttpMethod.POST,"/api/books")
                    .permitAll()

                /*
                 * Book Model controller
                 */

                .antMatchers("/api/book_models")
                    .authenticated()

                /*
                 * Exchange controller
                 */

                .antMatchers("/api/exchanges/**")
                    .authenticated()

                .antMatchers(HttpMethod.GET, "/api/exchanges")
                    .access(EXCHANGES_USER_ACCESS)

                .antMatchers(HttpMethod.GET, "/api/exchanges/{id:\\d+}")
                    .access(EXCHANGES_ACCESS)

                .antMatchers(HttpMethod.GET, "/api/exchanges/{id:\\d+}/messages", "/api/exchanges/{id:\\d+}/messages/**")
                    .access(EXCHANGES_ACCESS)

                /*
                 * Publication controller
                 */

                .antMatchers(HttpMethod.POST, "/api/publications")
                    .authenticated()

                .antMatchers(HttpMethod.GET, "/api/publications")
                    .access(PUBLICATIONS_GENERAL_ACCESS)

                .antMatchers(HttpMethod.DELETE, "/api/publications/{publication_id:\\d+}")
                    .access(PUBLICATION_MODIFY_ACCESS)

                .antMatchers(HttpMethod.GET, "/api/publications/{publication_id:\\d+}")
                    .access(PUBLICATION_ACCESS)

                .antMatchers( "/api/publications/{publication_id}/favorite", "/api/publications/{publication_id}/favorite/**")
                    .authenticated()

                .antMatchers(HttpMethod.GET, "/api/publications/{publication_id:\\d+}/favorite/{favorite_id:\\+}")
                    .access(PUBLICATION_FAVORITE_LIST_ACCESS)

                .antMatchers(HttpMethod.GET, "/api/publications/{publication_id:\\d+}/favorite")
                    .access(PUBLICATION_FAVORITE_ACCESS)


                /*
                 * User controller
                 */

                .antMatchers("/api/users/{id:\\d+}/locations/{location_id:\\d+}", "/api/users/{id:\\d+}/locations/{location_id:\\d+}",
                        "/api/users/{id:\\d+}/reviews", "/api/users/{id:\\d+}/reviews/{ur_id:\\d+}")
                    .authenticated()


                .antMatchers("/api/users/{id:\\d+}/locations/{location_id:\\d+}")
                    .access(USER_ACCESS)

                .antMatchers(HttpMethod.GET, "/api/users/{id:\\d+}/reviews/{ur_id:\\d+}")
                    .access(REVIEW_ACCESS)

                .antMatchers("/api/**").permitAll()


        //
        // .antMatchers("api/users/test").authenticated()
        //.anyRequest().permitAll() // Other endpoints can be accessed freely

        /*.antMatchers("/images/**, /css/**").permitAll() // Esto vuela porque se encarga el front
        .antMatchers("/api/auth/login").permitAll()  // Allow login endpoint without authentication
        .antMatchers("/", "/publications/*").permitAll()
        .antMatchers("/favicon.ico").permitAll()    // Esto vuela porque se encarga el front
        .antMatchers("/user_auth").anonymous()      // Esto vuela porque se encarga el front
        .antMatchers("/create","/login","/mail_input", "/change_password", "/success_registration", "/mail_input_message").anonymous()
        .antMatchers("/book","/book/**", "/start_exchange", "/profile", "/requests", "/offers", "/send_message", "/submit_review", "/like/**", "/success_password").authenticated()
        .antMatchers("/**").permitAll()*/
        .and()
        .headers().cacheControl().disable().and()

        // JWT Authentication Filter - Validates the JWT token
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

        // Custom Authentication Filter - Applied after the JWT filter
        .addFilterBefore(basicAuthTokenIssuerFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
