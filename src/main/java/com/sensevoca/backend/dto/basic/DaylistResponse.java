package com.sensevoca.backend.dto.basic;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DaylistResponse {
    private Long daylistId;
    private Long basicId;
    private String daylistTitle;
    private LocalDateTime latestAccess;
    private long daywordCount;

    public DaylistResponse(Long daylistId, Long basicId, String daylistTitle, LocalDateTime latestAccess, long daywordCount)
    {
        this.daylistId = daylistId;
        this.basicId = basicId;
        this.daylistTitle = daylistTitle;
        this.latestAccess = latestAccess;
        this.daywordCount = daywordCount;
    }
}
