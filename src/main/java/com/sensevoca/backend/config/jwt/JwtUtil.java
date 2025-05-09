package com.sensevoca.backend.config.jwt;

import com.sensevoca.backend.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;


import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class JwtUtil {

    private final Logger LOGGER = Logger.getLogger(JwtUtil.class.getName());

    @Value("${spring.jwt.access-secret}")
    private String accessSecretKey;
    @Value("${spring.jwt.refresh-secret}")
    private String refreshSecretKey;

    // Access Token 생성
    public String createAccessToken(User user, Long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getUserId());
        claims.put("username", user.getNickName());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTimeMs);

        String token = Jwts.builder()
                // 헤더
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // 페이로드
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                // 서명
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)
                .compact();

        return token;
    }

    // Refresh Token 생성
    public String createRefreshToken(User user, Long expireTimeMs) {
        Claims claims = Jwts.claims();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTimeMs);

        String token = Jwts.builder()
                // 헤더
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // 페이로드
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                // 서명
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();

        return token;
    }

    // 토큰 검증
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(accessSecretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            LOGGER.info("[validateToken] Error: " + e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(refreshSecretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            LOGGER.info("[validateToken] Error: " + e.getMessage());
            return false;
        }
    }

    // 토큰으로부터 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                getUserId(token),
                token,
                authorities
        );
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(accessSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
