package com.ruben.rrhh.talency.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mi_clave_super_segura_min_64_chars_1234567890123456}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24h por defecto
    private long expiration;

    @Value("${jwt.refresh.expiration:604800000}") // 7 días por defecto
    private long refreshExpiration;

    public String generateToken(UserDetails userDetails, String company) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("company", company);

        // Extraer solo el username del composite (company|username)
        String compositeUsername = userDetails.getUsername();
        String username = compositeUsername.split("\\|")[1];
        claims.put("username", username);

        // Extraer roles sin el prefijo ROLE_
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", ""))
                .toList();
        claims.put("roles", roles);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername()) // Guardamos el composite
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
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

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Long getExpirationTime() {
        return expiration;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}