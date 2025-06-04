package com.sensevoca.backend.dto.favoriteword;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordIdTypeRequest {
    private Long wordId;
    private String type;
}
