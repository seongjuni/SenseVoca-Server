package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.user.AddUserRequest;
import com.sensevoca.backend.dto.user.LoginUserRequest;
import com.sensevoca.backend.dto.user.LoginUserResponse;
import com.sensevoca.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "user", description = "유저 API")
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "유저 회원가입")
    public ResponseEntity<ResponseDTO<Long>> signup(@RequestBody AddUserRequest request) {
        Long savedId = userService.save(request);
        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("User registration successful.");
        response.setData(savedId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인")
    public ResponseEntity<ResponseDTO<LoginUserResponse>> login(@RequestBody LoginUserRequest request) {
        LoginUserResponse loginResponse = userService.login(request);
        ResponseDTO<LoginUserResponse> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("User login successful.");
        response.setData(loginResponse);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

//    @PostMapping("/oauth/kakao")
//    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestBody KakaoLoginRequestDto requestDto) {
//        return ResponseEntity.ok(authService.kakaoLogin(requestDto));
//    }
//
//    @PostMapping("/oauth/google")
//    public ResponseEntity<LoginResponseDto> googleLogin(@RequestBody GoogleLoginRequestDto requestDto) {
//        return ResponseEntity.ok(authService.googleLogin(requestDto));
//    }
}
