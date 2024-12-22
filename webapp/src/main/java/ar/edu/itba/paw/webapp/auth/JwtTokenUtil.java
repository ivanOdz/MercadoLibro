package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;


@Component
public class JwtTokenUtil {

    private final SecretKey jwtSigningKey;

    private static final String BEARER_PREFIX = "Bearer ";

    private static final int EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; //1 week (in millis)

    public JwtTokenUtil(@Value("classpath:jwt.key") Resource jwtKeyResource) throws IOException {
        byte[] keyBytes = FileCopyUtils.copyToByteArray(jwtKeyResource.getInputStream());
        this.jwtSigningKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication userAuth) {
        PawUserDetails pud = (PawUserDetails) userAuth.getPrincipal();
        User user = pud.getUser();

        Claims claims = Jwts.claims();

        claims.setSubject(user.getUsername());

        //claims.put("authorization", user.getRole());
        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(jwtSigningKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String parseToken(String header) {
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }

        final String token = header.substring(BEARER_PREFIX.length());   // Remove "Bearer "

        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSigningKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }

        return claims.getSubject();
    }
}