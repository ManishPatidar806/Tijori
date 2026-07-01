package com.financialapplication.tijori.Util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.access-expiration:900000}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration:2592000000}")
    private long refreshExpiration;

    private Key secretKey;

    @PostConstruct
    public void init() {
        if (secretKeyString == null || secretKeyString.length() < 64) {
            throw new IllegalStateException("JWT secret must be at least 64 characters for HS512");
        }
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }


    public String generateAccessToken(String mobileNo, String email) {
        String token = buildToken(mobileNo, email, "access", accessExpiration);
        log.info("Access token generated for user: {}", mobileNo);
        return token;
    }

    public String generateRefreshToken(String mobileNo) {
        String token = buildToken(mobileNo, null, "refresh", refreshExpiration);
        log.info("Refresh token generated for user: {}", mobileNo);
        return token;
    }

    private String buildToken(String mobileNo, String email, String tokenType, long expirationMs) {
        var builder = Jwts.builder()
                .setSubject(mobileNo)
                .setId(UUID.randomUUID().toString())
                .claim("type", tokenType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs));

        if (email != null && !email.isBlank()) {
            builder.claim("email", email);
        }

        return builder.signWith(secretKey).compact();
    }

    public boolean validateAccessToken(String token, String mobileNo) {
        try {
            Claims claims = extractClaims(token);
            return "access".equals(claims.get("type", String.class)) && mobileNo.equals(claims.getSubject());
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return "refresh".equals(claims.get("type", String.class));
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    public String extractTokenId(String token) {
        return extractClaims(token).getId();
    }

    public long getAccessExpirationMs() {
        return accessExpiration;
    }

    public long getRefreshExpirationMs() {
        return refreshExpiration;
    }

    public boolean validateToken(String token, String mobileNo) {
        return validateAccessToken(token, mobileNo);
    }

    public String extractMobile(String token)  {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }


    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}