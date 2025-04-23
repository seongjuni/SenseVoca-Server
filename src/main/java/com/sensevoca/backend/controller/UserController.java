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
import org.springframework.web.bind.annotation.*;

@Tag(name = "users", description = "유저 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "유저 회원가입")
    public ResponseEntity<ResponseDTO<Boolean>> signup(@RequestBody AddUserRequest request) {
        boolean success = userService.save(request);

        ResponseDTO<Boolean> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage(success
                            ? "User registration successful."
                            : "Email already exists.");
        response.setData(success);

        return ResponseEntity
                .status(success ? HttpStatus.CREATED : HttpStatus.CONFLICT)
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

    @GetMapping("/check-email")
    @Operation(summary = "아이디 중복 체크")
    public ResponseEntity<ResponseDTO<Boolean>> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);

        ResponseDTO<Boolean> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage(isDuplicate
                            ? "This email is already in use."
                            : "This email is available.");
        response.setData(isDuplicate);

        return ResponseEntity.ok(response);
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
