package com.rental.houserental.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private Key getSignKey() {
        String SECRET = "mySuperSecretKeyForJwtAuthentication2026SecureKey";
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generate token with email + role
    public String generateToken(String email, String role) {
        long EXPIRATION = 1000 * 60 * 60; // 1 hour
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)  // add role in JWT
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        return getClaims(token).getExpiration().after(new Date());
    }
}