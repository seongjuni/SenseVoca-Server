package com.sensevoca.backend.dto.favoriteword;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetFavoriteWordInfoRequest {
    private List<WordIdTypeRequest> wordIdTypes;
    private String phoneticType;
}
