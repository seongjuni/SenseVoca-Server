package com.sensevoca.backend.dto.signup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String user_id;         // 로그인 ID
    private String user_name;   // 닉네임
    private String user_password;   // 비밀번호
    private String user_login_type;
}
