package com.hamzaeren.course.spring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // application.properties'den değerleri okur.
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        // HMAC-SHA algoritmalarıyla kullanılabilecek anahtar oluşturur.
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Token üretirken artık username yerine id kullanıyoruz.
    // id asla değişmediği için username değişse bile token geçerli kalır.
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).getSubject());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String tokenSubject = extractAllClaims(token).getSubject();
        return tokenSubject.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        // Cihazın sistem saatini baz alır. Profesyonel uygulamalarda tüm sunucular
        // NTP (Network Time Protocol) kullanılarak merkezi zaman sunucusuyla senkronize
        // edilir.
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}