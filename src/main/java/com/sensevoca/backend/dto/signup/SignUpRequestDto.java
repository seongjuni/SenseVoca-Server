package com.sensevoca.backend.dto.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String user_id;             // 유저아이디
    private String user_name;           // 유저이름
    private String user_password;       // 유저비밀번호
    private String user_login_type;     // 로그인타입
}
