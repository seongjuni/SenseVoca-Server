package com.sensevoca.backend.dto.mywordbook;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMyWordInfoResponse {
    private Long mnemonicId;
    private String word;             // 영어 단어
    private String meaning;          // 뜻
    private String phoneticSymbol;   // 발음기호 (us, uk, aus 중 요청에 따라)
    private String association;      // 경선식 예문
    private String imageUrl;         // 이미지 URL
    private String exampleEng;       // 영어 예문
    private String exampleKor;       // 예문 번역
    private boolean favorite;        // 즐겨찾기 여부
}
