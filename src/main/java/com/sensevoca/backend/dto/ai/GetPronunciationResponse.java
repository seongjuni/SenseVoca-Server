package com.sensevoca.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPronunciationResponse {
    private String word;
    private OverallScore overallScore;
    private List<PhonemeResult> phonemeResults;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverallScore
    {
        private double accuracy;
        private double fluency;
        private double completeness;
        private double total;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhonemeResult
    {
        private String symbol;
        private double score;
        private String feedback;
    }
}
