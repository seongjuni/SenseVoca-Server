package com.sensevoca.backend.entity.basic;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "words")
@Getter
public class Words {

    // [멤버 필드]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long wordId;

    @OneToOne
    @JoinColumn(name = "mnemonic_id")
    private MnemonicExample mnemonicExample;

    @OneToOne(mappedBy = "words")
    private Dayword dayword;

    // [생성자]
    public Words() { }

}
