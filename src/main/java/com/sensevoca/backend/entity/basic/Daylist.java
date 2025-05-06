package com.sensevoca.backend.entity.basic;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "daylist")
@Getter
public class Daylist {

    // [멤버 필드]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daylist_id", nullable = false)
    private Long daylistId;

    @ManyToOne // Basic(1) <-> Daylist(N)
    @JoinColumn(name = "basic_id")
    Basic basic;

    @Column(name = "daylist_title", nullable = false)
    private String daylistTitle;

    @Column(name = "latest_access", nullable = false)
    private LocalDateTime latestAccess;

    @OneToMany(mappedBy = "daylist") // Daylist(1) <-> Dayword(N)
    List<Dayword> daywordList;

    // [생성자]
    public Daylist() {}
}
