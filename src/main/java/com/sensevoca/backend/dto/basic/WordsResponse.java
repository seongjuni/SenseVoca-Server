package com.sensevoca.backend.dto.basic;

import lombok.Getter;

@Getter
public class WordsResponse {
    // [멤버 필드]
    private Long wordId;
    private String word;
    private String meaning;
    private String partOfSpeech;
    private String phoneticUs;
    private String phoneticUk;
    private String phoneticAus;
    private String association;
    private String imageUrl;
    private String exampleEng;
    private String exampleKor;

    // [생성자]
    public WordsResponse(Long wordId, String word, String meaning, String partOfSpeech, String phoneticUs, String phoneticUk, String phoneticAus, String association, String imageUrl, String exampleEng, String exampleKor)
    {
        this.wordId = wordId;
        this.word = word;
        this.meaning = meaning;
        this.partOfSpeech = partOfSpeech;
        this.phoneticUs = phoneticUs;
        this.phoneticUk = phoneticUk;
        this.phoneticAus = phoneticAus;
        this.association = association;
        this.imageUrl = imageUrl;
        this.exampleEng = exampleEng;
        this.exampleKor = exampleKor;
    }
}
