package com.financialapplication.tijori.Util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    private Key secretKey;

    @PostConstruct
    public void init() {
        if (secretKeyString == null || secretKeyString.length() < 64) {
            throw new IllegalStateException("JWT secret must be at least 64 characters for HS512");
        }
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }



    public String generateToken(String mobileNo, String email) {
        String token = Jwts.builder().setSubject(mobileNo).claim("email", email).
                setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();

        log.info("Token generated for user: {}", mobileNo);
        return token;
    }

    public boolean validateToken(String token, String mobileNo) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return mobileNo.equals(extractMobile(token));
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }

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