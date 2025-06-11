package com.sensevoca.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickName;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // ⭐ enum을 문자열로 저장 (ex: "KAKAO")
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "interest_id",
            foreignKey = @ForeignKey(
                    name = "fk_user_interest",
                    foreignKeyDefinition = "FOREIGN KEY (interest_id) REFERENCES interests(id) ON DELETE SET NULL"
            )
    )
    private Interest interest;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_learned_date", nullable = false)
    private LocalDate lastLearnedDate;

    @Column(name = "today_count", nullable = false)
    private int todayCount = 0;

    @Column(name = "streak_days", nullable = false)
    private int streakDays = 0;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastLearnedDate = LocalDate.now().minusDays(1);
    }
}