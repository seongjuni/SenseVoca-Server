package com.sensevoca.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserStatsResponse {
    private int todayCount;   // 오늘 학습한 단어 수
    private int streakDays;   // 연속 학습 일수
}
