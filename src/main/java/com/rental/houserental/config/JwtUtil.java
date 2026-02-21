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

    private Key getSignKey(){
        String SECRET = "mySuperSecretKeyForJwtAuthentication2026SecureKey";
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String email){
        long EXPIRATION = 1000 * 60 * 60;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token){
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token){
        return getClaims(token).getExpiration().after(new Date());
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



}
