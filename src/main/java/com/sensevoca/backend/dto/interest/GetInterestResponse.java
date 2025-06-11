package com.sensevoca.backend.dto.interest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetInterestResponse {
    private Long interestId;
    private String type; // 또는 name
}
