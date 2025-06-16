package com.sensevoca.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "my_word_mnemonic")
public class MyWordMnemonic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_word_mnemonic_id")
    private Long myWordMnemonicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "word_id",
            foreignKey = @ForeignKey(
                    name = "fk_myword_wordinfo",
                    foreignKeyDefinition = "FOREIGN KEY (word_id) REFERENCES word_info(word_id) ON DELETE CASCADE"
            )
    )
    private WordInfo wordInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id",
            foreignKey = @ForeignKey(
                    name = "fk_mnemonic_interest",
                    foreignKeyDefinition = "FOREIGN KEY (interest_id) REFERENCES interest(id) ON DELETE SET NULL"
            )
    )
    private Interest interest;

    @Column(name = "meaning", nullable = false, length = 100)
    private String meaning;

    @Column(name = "association", columnDefinition = "TEXT")
    private String association;

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
