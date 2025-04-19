package com.sensevoca.backend.service;

import com.sensevoca.backend.config.jwt.JwtUtil;
import com.sensevoca.backend.dto.login.LoginRequestDto;
import com.sensevoca.backend.dto.login.LoginResponseDto;
import com.sensevoca.backend.dto.signup.SignUpRequestDto;
import com.sensevoca.backend.dto.signup.SignUpResponseDto;
import com.sensevoca.backend.entity.User;
import com.sensevoca.backend.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public SignUpResponseDto signup(SignUpRequestDto requestDto) {
        if (userRepository.existsById(requestDto.getUser_id())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
                .user_id(requestDto.getUser_id())
                .user_name(requestDto.getUser_name())
                .user_password(requestDto.getUser_password())
                .user_login_type(requestDto.getUser_login_type())
                .build();

        userRepository.save(user);

        return SignUpResponseDto.builder()
                .user_id(user.getUser_id())
                .user_name(user.getUser_name())
                .message("회원가입 완료")
                .build();
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {

        System.out.println("요청 user_id: " + requestDto.getUser_id());
        System.out.println("요청 password: " + requestDto.getUser_password());

        // 1. 유저 아이디 조회
        User user = userRepository.findById(requestDto.getUser_id())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        // 2. 유저 비밀번호 검색
        if(!user.getUser_password().equals(requestDto.getUser_password())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 3. jwt 토큰 생성
        String accessToken = jwtUtil.createAccessToken(user);

        // 4. 만료 시간 계산
        Date expiresAt = new Date(System.currentTimeMillis() + 60 * 60 * 3 * 1000L); // 3시간

        // 5. 반환
        return LoginResponseDto.builder()
                .access_token(accessToken)
                .user_name(user.getUser_name())
                .user_login_type(user.getUser_login_type())
                .expires_at(expiresAt)
                .build();
    }
}
