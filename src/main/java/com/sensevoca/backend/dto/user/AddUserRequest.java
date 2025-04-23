package com.sensevoca.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {
    private String email;
    private String nickName;
    private String password;
    private Long interestId;
}