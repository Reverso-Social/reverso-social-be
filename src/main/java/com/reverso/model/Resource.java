package com.reverso.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ResourceType type; // GUIDE, REPORT, ARTICLE, VIDEO, OTHER

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "preview_image_url")
    private String previewImageUrl;

    @Column(name = "public")
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isPublic == null) {
            this.isPublic = false;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum ResourceType {
        GUIDE, REPORT, ARTICLE, VIDEO, OTHER
    }
}