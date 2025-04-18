package com.sensevoca.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    private String user_id;

    @Column(name = "user_name", nullable = false)
    private String user_name;

    @Column(name = "user_password", nullable = false)
    private String user_password;

    @Column(name = "user_login_type", nullable = false)
    private String user_login_type;

    @Column(name = "user_created_at", nullable = false, updatable = false)
    private LocalDateTime user_created_at;

    @PrePersist
    protected void onCreate() {
        this.user_created_at = LocalDateTime.now();
    }
}