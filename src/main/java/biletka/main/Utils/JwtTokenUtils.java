package biletka.main.Utils;

import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Component
public class JwtTokenUtils {
    @Value("${app.secret-key}")
    private String secret;

    @Value("${app.key-lifetime-minutes}")
    private Duration jwtLifetime;

    private final List<String> invalidToken = new ArrayList<>();

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        SecretKey keys = new SecretKeySpec(secret.getBytes(), 0, secret.getBytes().length, "HMACSHA256");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token)  {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claims = getAllClaimsFromToken(token);
        return objectMapper.convertValue(claims.get("roles"), new TypeReference<List<String>>() {
        });
    }

    private Claims getAllClaimsFromToken(String token) {
        if (invalidToken.contains(token)) {
            return Jwts.claims().build();
        }

        return Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

    public void invalidateToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            if (!invalidToken.contains(token)) {
                invalidToken.add(token);
            }
        }
    }
}
