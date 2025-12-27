package com.ruben.rrhh.talency.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "mi_clave_super_segura_min_64_chars_1234567890123456";

    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24h

    // Token con company + roles
    public String generateToken(UserDetails userDetails, String company) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("company", company);
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername()) // SOLO username
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractCompany(String token) {
        return extractAllClaims(token).get("company", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}