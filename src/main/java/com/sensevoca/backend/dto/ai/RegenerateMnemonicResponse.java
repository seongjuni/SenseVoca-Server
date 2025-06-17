package com.sensevoca.backend.dto.ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegenerateMnemonicResponse {
    private String association;    // 니모닉 예문
    private String imageUrl;       // AI 이미지 URL
}
