package com.sensevoca.backend.dto.favoriteword;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFavoriteWordsResponse {
    private Long wordId;
    private String word;
    private String meaning;
    private String type;
}
