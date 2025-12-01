package com.reverso.repository;

import com.reverso.model.ServiceFeature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceFeatureRepository extends JpaRepository<ServiceFeature, UUID> {
    
    Page<ServiceFeature> findByServiceId(UUID serviceId, Pageable pageable);
    
    List<ServiceFeature> findByServiceIdOrderBySortOrderAsc(UUID serviceId);
    
    long countByServiceId(UUID serviceId);
}