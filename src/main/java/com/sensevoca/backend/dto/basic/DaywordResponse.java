package com.sensevoca.backend.dto.basic;

import lombok.Getter;

@Getter
public class DaywordResponse {
    // [멤버 필드]
    private Long daywordId;
    private String word;
    private String partOfSpeech;
    private String meaning;

    // [생성자]
    public DaywordResponse(Long daywordId, String word, String partOfSpeech, String meaning)
    {
        this.daywordId = daywordId;
        this.word = word;
        this.partOfSpeech = partOfSpeech;
        this.meaning = meaning;
    }
}
