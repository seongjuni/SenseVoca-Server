package com.sensevoca.backend.dto.basicword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetBasicResponse {
    private Long basicId;
    private String basicTitle;
    private String basicType;
    private String basicOfferedBy;
    private Integer daylistCount;
}
