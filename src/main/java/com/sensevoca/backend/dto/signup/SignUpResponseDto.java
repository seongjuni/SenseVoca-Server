package com.sensevoca.backend.dto.signup;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponseDto {
    private String user_id;         // 유저아이디
    private String user_name;       // 유저이름
    private String message;         // 반환메세지
}
