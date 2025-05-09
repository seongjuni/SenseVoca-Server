package com.sensevoca.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "basic_word")
public class BasicWord {

    @Id
    @Column(name = "word_id")
    private Long wordId;

    @Column(nullable = false, length = 100)
    private String word;

    @Column(name = "association", columnDefinition = "TEXT")
    private String association;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "example_eng", columnDefinition = "TEXT")
    private String exampleEng;

    @Column(name = "example_kor", columnDefinition = "TEXT")
    private String exampleKor;
}
