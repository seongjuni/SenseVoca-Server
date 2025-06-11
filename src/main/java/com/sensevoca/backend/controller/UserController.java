package com.sensevoca.backend.controller;

import com.sensevoca.backend.domain.Interest;
import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.user.AddUserRequest;
import com.sensevoca.backend.dto.user.GetUserStatsResponse;
import com.sensevoca.backend.dto.user.LoginUserRequest;
import com.sensevoca.backend.dto.user.LoginUserResponse;
import com.sensevoca.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                            ? "유저 회원가입 성공"
                            : "이메일이 이미 존재합니다.");
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
        response.setMessage("유저 로그인 성공");
        response.setData(loginResponse);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{email}/check-email")
    @Operation(summary = "아이디 중복 체크")
    public ResponseEntity<ResponseDTO<Boolean>> checkEmailDuplicate(
            @PathVariable String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);

        ResponseDTO<Boolean> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage(isDuplicate
                            ? "이 이메일은 이미 사용중입니다."
                            : "이 이메일은 사용 가능합니다.");
        response.setData(isDuplicate);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping ("/{id}")
    @Operation(summary = "유저 회원탈퇴")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        ResponseDTO<Void> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("유저 삭제 성공");

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/status")
    @Operation(summary = "유저 학습 통계 조회")
    public ResponseEntity<ResponseDTO<GetUserStatsResponse>> getStatus() {
        GetUserStatsResponse stats = userService.getUserStats();

        ResponseDTO<GetUserStatsResponse> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("학습 통계 조회 성공");
        response.setData(stats);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/status/update/{learnedCount}")
    @Operation(summary = "유저 학습 통계 업데이트")
    public ResponseEntity<ResponseDTO<Void>> updateStatus(@PathVariable int learnedCount) {
        userService.updateUserLearnedStats(learnedCount);

        ResponseDTO<Void> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("학습 통계 업데이트 성공");

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
