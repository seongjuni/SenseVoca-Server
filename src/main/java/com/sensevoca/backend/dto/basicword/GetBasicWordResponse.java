package com.sensevoca.backend.dto.basicword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetBasicWordResponse {

    private Long daywordId;
    private String word;    // word_info
    private String meaning;
    private String association;
    private String imageUrl;
    private String exampleEng;
    private String exampleKor;
    private String phonetic;
    private boolean favorite;        // 즐겨찾기 여부
}
