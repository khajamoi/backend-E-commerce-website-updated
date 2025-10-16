package com.fruit_ecommerce_backend.fruit_ecommerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Role;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 bytes long");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String subject, Role role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role != null ? role.name() : Role.USER.name())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Validate JWT token */
    public boolean validateJwtToken(String token) {
        try {
            getParser().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // log: token expired
        } catch (UnsupportedJwtException e) {
            // log: unsupported JWT
        } catch (MalformedJwtException e) {
            // log: malformed JWT
        } catch (SignatureException e) {
            // log: invalid signature
        } catch (IllegalArgumentException e) {
            // log: empty or null token
        }
        return false;
    }

    /** Extract subject (e.g., email) */
    public String getEmailFromToken(String token) {
        return getParser().parseClaimsJws(token).getBody().getSubject();
    }

    /** Extract role */
    public String getRoleFromToken(String token) {
        Claims claims = getParser().parseClaimsJws(token).getBody();
        Object role = claims.get("role");
        return role != null ? role.toString() : Role.USER.name();
    }

    /** Build parser once */
    private JwtParser getParser() {
        return Jwts.parserBuilder().setSigningKey(key).build();
    }
}
