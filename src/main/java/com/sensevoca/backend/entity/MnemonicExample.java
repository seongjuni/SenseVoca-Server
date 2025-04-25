package com.sensevoca.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "mnemonic_example")
public class MnemonicExample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id",
            foreignKey = @ForeignKey(
                    name = "fk_mnemonic_interest",
                    foreignKeyDefinition = "FOREIGN KEY (interest_id) REFERENCES interests(id) ON DELETE SET NULL"
            )
    )
    private Interest interest;

    @Column(nullable = false, length = 100)
    private String word;

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

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "example_eng", columnDefinition = "TEXT")
    private String exampleEng;

    @Column(name = "example_kor", columnDefinition = "TEXT")
    private String exampleKor;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
