package com.sensevoca.backend.dto.mywordbook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GetMyWordbookResponse {
    private Long id;
    private String title;
    private int wordCount;
    private LocalDateTime lastAccessedAt;
}
