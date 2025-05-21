package com.sensevoca.backend.dto.basicword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetDaywordResponse {

    private Long daywordId;
    private String word;
    private String meaning;
}
