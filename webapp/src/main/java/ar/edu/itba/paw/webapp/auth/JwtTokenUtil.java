package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;


@Component
public class JwtTokenUtil {

    private final SecretKey jwtSigningKey;

    private static final String BEARER_PREFIX = "Bearer ";

    private static final String ACCESS_TOKEN = "access_token";

    private static final String REFRESH_TOKEN = "refresh_token";

    public static final String ACCESS_TOKEN_HEADER = "X-Access-Token";

    public static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";

    private static final int REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; //1 week (in seconds)

    private static final int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000; // 1h

    public JwtTokenUtil(@Value("classpath:jwt.key") Resource jwtKeyResource) throws IOException {
        byte[] keyBytes = FileCopyUtils.copyToByteArray(jwtKeyResource.getInputStream());
        this.jwtSigningKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(User user) {
        return createToken(user, ACCESS_TOKEN_EXPIRATION_TIME, ACCESS_TOKEN);
    }

    public String createRefreshToken(User user) {
        return createToken(user, REFRESH_TOKEN_EXPIRATION_TIME, REFRESH_TOKEN);
    }

    private String createToken(User user, int expirationTime, String type) {
        Claims claims = Jwts.claims();

        claims.setSubject(user.getUserId().toString());
        claims.put("name", user.getUsername());
        claims.put("type", type);

        //claims.put("authorization", user.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(jwtSigningKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String removePrefix(String header) {
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return header.substring(BEARER_PREFIX.length());
    }

    public boolean isRefreshToken(String header) {
        final String token = removePrefix(header);

        if(token == null) {
            return false;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSigningKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("type").equals(REFRESH_TOKEN);
        } catch (Exception e) {
            return false;
        }
    }

    public String parseToken(String header) {
        final String token = removePrefix(header);

        if(token == null) {
            return null;
        }

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