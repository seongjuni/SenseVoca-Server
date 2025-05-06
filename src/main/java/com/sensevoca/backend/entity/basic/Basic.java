package com.sensevoca.backend.entity.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "basic")
@Getter
@JsonIgnoreProperties({"hibernatelazyInitializer", "handler"})
public class Basic {
    // [멤버 필드]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basic_id", nullable = false)
    private Long basicId;

    @Column(name = "basic_title", nullable = false)
    private String basicTitle;

    @Column(name = "basic_type", nullable = false)
    private String basicType;

    @Column(name = "basic_offered_by", nullable = false)
    private String basicOfferedby;

    @OneToMany(mappedBy = "basic") // Basic <-> Daylist
    private List<Daylist> dayList;

    // [생성자]
    public Basic() { }
}