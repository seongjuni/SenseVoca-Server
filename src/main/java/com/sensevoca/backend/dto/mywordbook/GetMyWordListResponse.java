package com.sensevoca.backend.dto.mywordbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMyWordListResponse {
    private Long myWordId;
    private String word;         // 단어
    private String meaning;      // 뜻
}
