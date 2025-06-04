package com.sensevoca.backend.dto.favoriteword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteWordDetailResponse {
    private String type;  // "MY" 또는 "BASIC"
    private Object data;  // GetMyWordInfoResponse 또는 GetBasicWordResponse
}