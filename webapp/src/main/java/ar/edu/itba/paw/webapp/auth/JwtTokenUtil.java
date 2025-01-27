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
import java.util.Date;


@Component
public class JwtTokenUtil {

    private final SecretKey jwtSigningKey;

    private static final String BEARER_PREFIX = "Bearer ";

    public static final String ACCESS_TOKEN_HEADER = "X-Access-Token";

    public static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";

    private static final int REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 ; //1 week (in seconds)

    private static final int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000; // 1h

    public JwtTokenUtil(@Value("classpath:jwt.key") Resource jwtKeyResource) throws IOException {
        byte[] keyBytes = FileCopyUtils.copyToByteArray(jwtKeyResource.getInputStream());
        this.jwtSigningKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(User user) {
        return createToken(user, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String createRefreshToken(User user) {
        return createToken(user, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    // Authentication userAuth
    private String createToken(User user, int expirationTime) {
        //PawUserDetails pud = (PawUserDetails) userAuth.getPrincipal();
        //User user = pud.getUser();

        Claims claims = Jwts.claims();

        claims.setSubject(user.getUserId().toString());
        claims.put("name", user.getUsername()); 

        //claims.put("authorization", user.getRole());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
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

        } catch(ExpiredJwtException e) {
            // TODO: Analizar refreshToken en HttpOnl Cookie
            // Si es valida, adjuntar a la response nuevo access token y nuevo refresh token,
            // caso contrario retorno null
            return null;
        }
        catch (Exception e) {
            return null;
        }

        return claims.getSubject();
    }
}