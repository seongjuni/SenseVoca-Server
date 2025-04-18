package com.sensevoca.backend.service;

import com.sensevoca.backend.dto.signup.SignUpRequestDto;
import com.sensevoca.backend.dto.signup.SignUpResponseDto;
import com.sensevoca.backend.entity.User;
import com.sensevoca.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

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
}
