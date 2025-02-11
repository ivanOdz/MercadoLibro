package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthTokenIssuerFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    private final BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            try {
                Authentication maybeUser = authenticationConverter.convert(request);

                if (maybeUser != null) {
                    String username = maybeUser.getName();
                    String password = (String) maybeUser.getCredentials();

                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, password)
                    );

                    PawUserDetails userDetails = (PawUserDetails) userDetailsService.loadUserByUsername(username);

                    if(!userDetails.getUser().isVerified()){
                        filterChain.doFilter(request, response);
                        return;
                    }

                    Authentication authenticationSuccessfull = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            password,
                            userDetails.getAuthorities()
                    );

                    User user = userDetails.getUser();
                    String accessToken = jwtTokenUtil.createAccessToken(user);
                    String refreshToken = jwtTokenUtil.createRefreshToken(user);

                    response.addHeader(JwtTokenUtil.ACCESS_TOKEN_HEADER, accessToken);
                    response.addHeader(JwtTokenUtil.REFRESH_TOKEN_HEADER, refreshToken);
                    response.addHeader("X-User-URI", "/users/" + user.getUserId());

                    SecurityContextHolder.getContext().setAuthentication(authenticationSuccessfull);
                }
            } catch (AuthenticationException e) {
                SecurityContextHolder.clearContext();
                authenticationEntryPoint.commence(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
