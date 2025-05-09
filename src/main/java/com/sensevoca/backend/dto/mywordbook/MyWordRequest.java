package com.sensevoca.backend.dto.mywordbook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyWordRequest {
    private Long wordId;
    private String word;
    private String meaning;
}
