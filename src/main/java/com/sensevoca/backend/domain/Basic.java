package com.sensevoca.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "basic")
public class Basic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basic_id", nullable = false)
    private Long basicId;

    @Column(name = "basic_title", nullable = false)
    private String basicTitle;

    @Column(name = "basic_type", nullable = false)
    private String basicType;

    @Column(name = "basic_offered_by", nullable = false)
    private String basicOfferedBy;
}
