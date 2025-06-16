package com.sensevoca.backend.service;

import com.sensevoca.backend.config.jwt.JwtUtil;
import com.sensevoca.backend.dto.user.AddUserRequest;
import com.sensevoca.backend.dto.user.GetUserStatsResponse;
import com.sensevoca.backend.dto.user.LoginUserRequest;
import com.sensevoca.backend.dto.user.LoginUserResponse;
import com.sensevoca.backend.domain.Interest;
import com.sensevoca.backend.domain.LoginType;
import com.sensevoca.backend.domain.RefreshToken;
import com.sensevoca.backend.domain.User;
import com.sensevoca.backend.repository.InterestRepository;
import com.sensevoca.backend.repository.RefreshTokenRepository;
import com.sensevoca.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
                .nickname(user.getNickName())
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

    public GetUserStatsResponse getUserStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        LocalDate today = LocalDate.now();
        LocalDate lastDate = user.getLastLearnedDate();

        int visibleStreak = user.getStreakDays();
        if (lastDate != null && lastDate.isBefore(today.minusDays(1))) {
            visibleStreak = 0; // 계산만 하고 저장은 하지 않음
        }

        return GetUserStatsResponse.builder()
                .todayCount(user.getTodayCount())
                .streakDays(visibleStreak)
                .build();
    }

    @Transactional
    public void updateUserLearnedStats(int learnedCount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        LocalDate today = LocalDate.now();
        LocalDate lastDate = user.getLastLearnedDate();

        // 🎯 streakDays 처리
        if (lastDate == null) {
            user.setStreakDays(1); // 첫 학습
        } else if (lastDate.equals(today.minusDays(1))) {
            user.setStreakDays(user.getStreakDays() + 1); // 연속 학습
        } else if (!lastDate.equals(today)) {
            user.setStreakDays(1); // 연속 실패 또는 오늘 첫 학습
        }

        // 🎯 날짜, 학습 단어 수 갱신
        user.setLastLearnedDate(today);
        user.setTodayCount(user.getTodayCount() + learnedCount);

        userRepository.save(user);
    }
}
