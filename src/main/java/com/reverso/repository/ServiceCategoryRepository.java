package com.reverso.repository;

import com.reverso.model.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, UUID> {
    
    Page<ServiceCategory> findByActive(Boolean active, Pageable pageable);
    
    List<ServiceCategory> findByActiveOrderBySortOrderAsc(Boolean active);
    
    Page<ServiceCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);
}