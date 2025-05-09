package com.sensevoca.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetWordPhoneticsResponse {
    private String word;
    private String phoneticUs;
    private String phoneticUk;
    private String phoneticAus;
}
