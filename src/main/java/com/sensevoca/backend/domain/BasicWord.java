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
    @Column(name = "basic_word_id")
    private Long basicWordId;

    @OneToOne
    @JoinColumn(name = "word_id")
    private WordInfo wordInfo;

    @Column(name = "meaning", nullable = false)
    private String meaning;

    @Column(name = "association", nullable = false, columnDefinition = "TEXT")
    private String association;

    @Column(name = "image_url", nullable = false , columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "example_eng", nullable = false , columnDefinition = "TEXT")
    private String exampleEng;

    @Column(name = "example_kor", nullable = false , columnDefinition = "TEXT")
    private String exampleKor;

}
