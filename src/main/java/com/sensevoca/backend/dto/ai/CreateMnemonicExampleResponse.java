package com.sensevoca.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMnemonicExampleResponse {
    private String meaning;
    private String association;    // 니모닉 예문
    private String imageUrl;       // AI 이미지 URL
    private String exampleEng;     // 영어 예문
    private String exampleKor;     // 한글 해석 예문
}
