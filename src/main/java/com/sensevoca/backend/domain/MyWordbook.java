package com.sensevoca.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "my_wordbook")
public class MyWordbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_wordbook_id")
    private Long myWordbookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_user_wordbook",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
            )
    )
    private User user;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "word_count", nullable = false)
    private int wordCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastAccessedAt = this.createdAt;
    }

    public void updateLastAccessed() {
        this.lastAccessedAt = LocalDateTime.now();
    }
    
    public void updateTitle(String newTitle) { this.title = newTitle; }
}
