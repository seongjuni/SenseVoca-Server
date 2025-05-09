package com.sensevoca.backend.dto.mywordbook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetMyWordInfoRequest {
    private List<Long> wordIds;
    private String phoneticType;
}