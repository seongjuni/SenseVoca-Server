package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.signup.SignUpRequestDto;
import com.sensevoca.backend.dto.signup.SignUpResponseDto;
import com.sensevoca.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = authService.signup(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
