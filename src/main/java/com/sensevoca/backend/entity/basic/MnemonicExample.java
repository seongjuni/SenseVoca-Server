package com.sensevoca.backend.entity.basic;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "mnemonic_example")
@Getter
public class MnemonicExample {

    // [멤버 필드]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long mnemonicId;

    @Column(name = "word")
    private String word;

    @Column(name = "meaning")
    private String meaning;

    @Column(name = "part_of_speech")
    private String partOfSpeech;

    @Column(name = "phonetic_us")
    private String phoneticUs;

    @Column(name = "phonetic_uk")
    private String phoneticUk;

    @Column(name = "phonetic_aus")
    private String phoneticAus;

    @Column(name = "association")
    private String association;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "example_eng")
    private String exampleEng;

    @Column(name = "example_kor")
    private String exampleKor;

    // [생성자]
    public MnemonicExample() { }
}
