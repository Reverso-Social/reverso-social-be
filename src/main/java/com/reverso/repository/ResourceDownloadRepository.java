package com.reverso.repository;

import com.reverso.model.ResourceDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResourceDownloadRepository extends JpaRepository<ResourceDownload, UUID> {
    
    List<ResourceDownload> findByUserId(UUID userId);
    
    List<ResourceDownload> findByResourceId(UUID resourceId);
    
    long countByResourceId(UUID resourceId);
    
    boolean existsByUserIdAndResourceId(UUID userId, UUID resourceId);
}