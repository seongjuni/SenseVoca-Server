package com.sensevoca.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "my_word")
public class MyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wordbook_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_wordbook_word", foreignKeyDefinition = "FOREIGN KEY (wordbook_id) REFERENCES my_wordbook(id) ON DELETE CASCADE"))
    private MyWordbook wordbook;

    @Column(nullable = false, length = 100)
    private String spelling;

    @Column(nullable = false, length = 255)
    private String meaning;

    @Column(name = "part_of_speech", length = 50)
    private String partOfSpeech;

    @Column(name = "phonetic_us", length = 100)
    private String phoneticUs;

    @Column(name = "phonetic_uk", length = 100)
    private String phoneticUk;

    @Column(name = "phonetic_aus", length = 100)
    private String phoneticAus;
}