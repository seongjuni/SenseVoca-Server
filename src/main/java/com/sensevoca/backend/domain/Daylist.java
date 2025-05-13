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
@Table(name = "daylist")
public class Daylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daylist_id", nullable = false)
    private Long daylistId;

    @ManyToOne
    @JoinColumn(name = "basic_id", nullable = false)
    private Basic basic;

    @Column(name = "daylist_title", nullable = false)
    private String daylistTitle;

    @Column(name = "latest_accessed_at")
    private LocalDateTime latestAccessedAt;

}
