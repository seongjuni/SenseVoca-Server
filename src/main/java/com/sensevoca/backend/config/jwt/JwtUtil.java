package com.sensevoca.backend.config.jwt;

import com.sensevoca.backend.entity.User;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;


import java.util.Date;

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
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME)) // 3시간
                .signWith(Keys.hmacShaKeyFor(accessSecretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
