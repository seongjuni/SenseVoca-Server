package com.sensevoca.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "dayword")
public class Dayword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dayword_id", nullable = false)
    private Long daywordId;

    @ManyToOne
    @JoinColumn(name = "daylist_id", nullable = false)
    private Daylist daylist;

    @OneToOne
    @JoinColumn(name = "basic_word_id", nullable = false)
    private BasicWord basicWord;

}
