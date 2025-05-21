package com.sensevoca.backend.dto.basicword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetDaylistResponse {

    private Long daylistId;
    private String daylistTitle;
    private LocalDateTime latestAccessedAt;
    private Integer daywordCount;

}
