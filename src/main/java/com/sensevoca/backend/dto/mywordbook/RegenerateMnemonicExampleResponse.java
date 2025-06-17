package com.sensevoca.backend.dto.mywordbook;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegenerateMnemonicExampleResponse {
    private String association;    // 니모닉 예문
    private String imageUrl;       // AI 이미지 URL
}
