package com.sensevoca.backend.service;

import com.sensevoca.backend.config.jwt.JwtUtil;
import com.sensevoca.backend.dto.user.AddUserRequest;
import com.sensevoca.backend.dto.user.LoginUserRequest;
import com.sensevoca.backend.dto.user.LoginUserResponse;
import com.sensevoca.backend.domain.Interest;
import com.sensevoca.backend.domain.LoginType;
import com.sensevoca.backend.domain.RefreshToken;
import com.sensevoca.backend.domain.User;
import com.sensevoca.backend.repository.InterestRepository;
import com.sensevoca.backend.repository.RefreshTokenRepository;
import com.sensevoca.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private Long accessExpireTimeMs = 60 * 60 * 1000L;  // 1시간
    private Long refreshExpireTimeMs = 14 * 24 * 60 * 60 * 1000L;  // 14일


    public boolean save(AddUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return false; // 중복이면 실패
        }

        Interest interest = interestRepository.findById(request.getInterestId())
                .orElseThrow(() -> new IllegalArgumentException("해당 관심사가 존재하지 않습니다."));

        userRepository.save(User.builder()
                .email(request.getEmail())
                .nickName(request.getNickName())
                .password(passwordEncoder.encode(request.getPassword()))
                .loginType(LoginType.NORMAL)
                .interest(interest)
                .build());

        return true;
    }

    public LoginUserResponse login(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("email 존재하지 않음"));
        // password 틀림
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("password 틀림");
        }

        String accessToken = jwtUtil.createAccessToken(user, accessExpireTimeMs);
        String refreshToken = jwtUtil.createRefreshToken(user, refreshExpireTimeMs);

        refreshTokenRepository.findByUserId(user.getUserId())
                .ifPresentOrElse(
                        token -> refreshTokenRepository.save(token.update(refreshToken)),
                        () -> refreshTokenRepository.save(new RefreshToken(user.getUserId(), refreshToken))
                );

        return LoginUserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("email 존재하지 않음"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 사용자가 존재하지 않습니다."));
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
