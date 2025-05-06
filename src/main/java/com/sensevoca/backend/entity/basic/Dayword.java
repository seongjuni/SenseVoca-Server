package com.sensevoca.backend.entity.basic;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Table(name = "dayword")
@Getter
public class Dayword {

    // [멤버 필드]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dayword_id")
    private Long daywordId;

    @ManyToOne // Daylist(1) <-> Dayword(N)
    @JoinColumn(name = "daylist_id")
    private Daylist daylist;

    @OneToOne
    @JoinColumn(name = "word_id")
    private Words words;

    // [생성자]
    public Dayword() { }

}
