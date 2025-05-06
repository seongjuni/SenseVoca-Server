package com.sensevoca.backend.dto.basic;

import lombok.Getter;

@Getter
public class MnemonicResponse {
    // [멤버 필드]
    private Long wordId;            // word_id
    private String word;            // 단어
    private String meaning;         // 뜻
    private String partOfSpeech;    // 품사
    private String phoneticUs;      // 미국 발음
    private String phoneticUk;      // 영국 발음
    private String phoneticAus;     // 호주 발음
    private String association;     // 니모닉
    private String imageUrl;        // 이미지 경로
    private String exampleEng;      // 영어 예문
    private String exampleKor;      // 한글 예문

    // [생성자]
    public MnemonicResponse(Long wordId, String word, String meaning, String partOfSpeech, String phoneticUs, String phoneticUk, String phoneticAus, String association, String imageUrl, String exampleEng, String exampleKor)
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
