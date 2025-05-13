package com.sensevoca.backend.dto.basicword;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateDatetimeRequest {
    private LocalDateTime latestAccessedAt;
}
