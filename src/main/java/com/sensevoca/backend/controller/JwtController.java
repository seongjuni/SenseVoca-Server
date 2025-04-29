package com.sensevoca.backend.controller;

import com.sensevoca.backend.config.jwt.JwtFilter;
import com.sensevoca.backend.dto.user.CreateAccessTokenRequest;
import com.sensevoca.backend.dto.user.CreateAccessTokenResponse;
import com.sensevoca.backend.service.JwtService;
import com.sensevoca.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "jwt", description = "jwt API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String newAccessToken = jwtService.createNewAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}