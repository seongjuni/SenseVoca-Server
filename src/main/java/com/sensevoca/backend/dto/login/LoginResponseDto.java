package com.sensevoca.backend.dto.login;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String access_token;
//    private String refreshToken;
    private String user_name;
    private String user_login_type;
    private Date expires_at;
}
