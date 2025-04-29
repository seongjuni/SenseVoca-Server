package com.sensevoca.backend.domain;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mnemonic_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_word_mnemonic", foreignKeyDefinition = "FOREIGN KEY (mnemonic_id) REFERENCES mnemonic_example(id) ON DELETE CASCADE"))
    private MnemonicExample mnemonic;
}