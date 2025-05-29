package com.sensevoca.backend.dto.wordinfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetWordInfosResponse {
    private Long wordId;             // 단어 id
    private String word;             // 영어 단어
    private String meaning;          // 뜻
}
