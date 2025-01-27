package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class BasicAuthTokenIssuerFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public BasicAuthTokenIssuerFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);

        User user = (User) authResult.getPrincipal();

        String accessToken = jwtTokenUtil.createAccessToken(user);
        String refreshToken = jwtTokenUtil.createRefreshToken(user);

        response.addHeader(JwtTokenUtil.ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(JwtTokenUtil.REFRESH_TOKEN_HEADER, refreshToken);

        response.addHeader("X-User-URI", "users/" + user.getUserId());
    }
}

/*
    1) Devolver via header al autenticarlo, el endpoint para pedir al usuario -> Podemos usar userid para Enpoints
    2) Una vez que se tiene los tokens, que angular haga get al /users/{username}
 */