package com.sensevoca.backend.dto.signup;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponseDto {
    private String user_id;
    private String user_name;
    private String message;
}
