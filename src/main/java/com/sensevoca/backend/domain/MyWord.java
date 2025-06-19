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
@Table(name = "my_word")
public class MyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_word_id")
    private Long myWordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "wordbook_id",
            foreignKey = @ForeignKey(
                    name = "fk_myword_wordbook",
                    foreignKeyDefinition = "FOREIGN KEY (wordbook_id) REFERENCES my_wordbook(my_wordbook_id) ON DELETE CASCADE"
            )
    )
    private MyWordbook myWordbook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "my_word_mnemonic_id",
            foreignKey = @ForeignKey(
                    name = "fk_myword_mnemonic",
                    foreignKeyDefinition = "FOREIGN KEY (my_word_mnemonic_id) REFERENCES my_word_mnemonic(my_word_mnemonic_id) ON DELETE CASCADE"
            )
    )
    private MyWordMnemonic myWordMnemonic;
}