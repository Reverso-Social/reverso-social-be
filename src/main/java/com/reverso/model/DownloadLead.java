package com.reverso.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "download_leads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadLead {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_downloaded_at")
    private LocalDateTime lastDownloadedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastDownloadedAt = LocalDateTime.now();
    }
}
