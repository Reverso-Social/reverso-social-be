package com.reverso.repository;

import com.reverso.model.DownloadLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DownloadLeadRepository extends JpaRepository<DownloadLead, UUID> {
    List<DownloadLead> findByResourceId(UUID resourceId);
    List<DownloadLead> findByEmail(String email);
    boolean existsByEmailAndResourceId(String email, UUID resourceId);
}