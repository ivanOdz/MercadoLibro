package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PawUserDetailsService pawUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String userIdString = jwtTokenUtil.parseToken(header);

        if (userIdString == null) {
            filterChain.doFilter(request, response);
            return;
        }

        long userIdNumber;
        try {
            userIdNumber = Long.parseLong(userIdString);
        } catch (NumberFormatException e){
            filterChain.doFilter(request, response);
            return;
        }

        PawUserDetails pawUserDetails;
        try {
            pawUserDetails = (PawUserDetails) pawUserDetailsService.loadUserById(userIdNumber);
        } catch (UserNotFoundException e){
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                pawUserDetails.getUser().getUsername(),
                pawUserDetails.getUser().getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        filterChain.doFilter(request, response);
    }
}
