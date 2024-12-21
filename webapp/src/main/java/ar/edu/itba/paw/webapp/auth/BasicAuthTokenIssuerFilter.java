package ar.edu.itba.paw.webapp.auth;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import org.springframework.security.oauth2.jwt.Jwt;

@Component
public class BasicAuthTokenIssuerFilter extends BasicAuthenticationFilter {

    public BasicAuthTokenIssuerFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);
        final String jwt;
        try {
            jwt = createJwt(authResult);
        } catch (Exception e) {
            // To call
            throw new IOException();
        }
        response.addHeader("Authorization", "Bearer " + jwt);
    }

    public String createJwt(Authentication authentication) throws Exception {

        // Build the claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(authentication.getName())
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000)) // Valid for 1 hour
                .claim("roles", authentication.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .toList())
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        // Sign it with the secret key
        // This sign throws
        signedJWT.sign(new MACSigner("secretKey"));

        // Serialize it to a compact token string
        return signedJWT.serialize();
    }

}
