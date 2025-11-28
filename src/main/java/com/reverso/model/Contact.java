package com.reverso.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String message;

    private Boolean acceptsPrivacy;

    private String status; // PENDING, IN_PROGRESS, RESOLVED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long userId; // admin who handled the ticket (optional)

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
