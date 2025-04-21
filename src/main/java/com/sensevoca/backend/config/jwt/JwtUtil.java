package com.sensevoca.backend.config.jwt;

import com.sensevoca.backend.entity.User;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;


import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {

    @Value("${spring.jwt.access-secret}")
    private String accessSecretKey;

//    @Value("${spring.jwt.refresh-secret}")
//    private String refreshSecretKey;

    private static final long TOKEN_EXPIRATION_TIME = 60 * 60 * 3 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 24 * 1000L;

    public String createAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUser_id())
                .claim("user_name", user.getUser_name())
                .claim("user_login_type", user.getUser_login_type())
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME)) // 3시간
                .signWith(Keys.hmacShaKeyFor(accessSecretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(accessSecretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token); // ← 여기서 형식, 서명, 만료 모두 검증
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String role = claims.get("role", String.class);

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()) // ROLE_USER or ROLE_ADMIN
        );

        return new UsernamePasswordAuthenticationToken(
                getUserId(token),
                token,
                authorities
        );
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(accessSecretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
