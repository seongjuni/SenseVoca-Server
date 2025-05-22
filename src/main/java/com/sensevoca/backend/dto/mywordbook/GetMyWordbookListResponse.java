package com.sensevoca.backend.dto.mywordbook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GetMyWordbookListResponse {
    private Long id;
    private String title;
    private int wordCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;
}
