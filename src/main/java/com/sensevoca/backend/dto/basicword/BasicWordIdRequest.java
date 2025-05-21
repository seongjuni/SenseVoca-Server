package com.sensevoca.backend.dto.basicword;

import lombok.Getter;

import java.util.List;

@Getter
public class BasicWordIdRequest {
    private List<Long> daywordId;
}
