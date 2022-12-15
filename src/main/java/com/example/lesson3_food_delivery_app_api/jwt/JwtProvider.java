package com.example.lesson3_food_delivery_app_api.jwt;

import com.example.lesson3_food_delivery_app_api.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class JwtProvider {

    private static final int TOKEN_EXPIRATION_INTERVAL = 1000 * 60 * 60 * 24 * 7; // 7 days
    private static final String SECRET = "asdsa214fasfm900adjocaose012i0kkkopokkopojfafsd22ad2";

    public static String generateToken(User user) {
        String email = user.getEmail();
        String role = user.getRole().name();

        Map<String, String> claims = Map.of("email", email, "role", role);
        Date expiredDate = new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_INTERVAL);
        String token = Jwts.builder()
                .setSubject(email)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        return token;
    }

    public static Claims getClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
}
