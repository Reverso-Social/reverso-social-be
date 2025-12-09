package com.reverso.model;

import com.reverso.model.enums.BlogPostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "blogposts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String category;

    private String coverImagePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BlogPostStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == BlogPostStatus.PUBLISHED) {
            this.publishedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();

        if (this.status == BlogPostStatus.PUBLISHED && this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }
}
