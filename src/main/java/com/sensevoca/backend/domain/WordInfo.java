package com.sensevoca.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "word_info")
public class WordInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long wordId;

    @Column(name = "word",nullable = false, length = 100)
    private String word;

    @Column(name = "phonetic_us", length = 100)
    private String phoneticUs;

    @Column(name = "phonetic_uk", length = 100)
    private String phoneticUk;

    @Column(name = "phonetic_aus", length = 100)
    private String phoneticAus;
}
