package com.sensevoca.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_word",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "my_word_mnemonic_id"}),
                @UniqueConstraint(columnNames = {"user_id", "dayword_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // my_word_mnemonic과 연관 (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_word_mnemonic_id")
    private MyWordMnemonic myWordMnemonic;

    // basic_word와 연관 (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dayword_id")
    private Dayword dayword;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @AssertTrue(message = "myWordMnemonic 또는 basicWord 중 하나만 있어야 합니다.")
    public boolean isOnlyOneSet() {
        return (myWordMnemonic != null && dayword == null)
                || (myWordMnemonic == null && dayword != null);
    }
}