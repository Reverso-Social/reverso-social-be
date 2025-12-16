package com.reverso.repository;

import com.reverso.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    
    Page<Service> findByActive(Boolean active, Pageable pageable);
    
    Page<Service> findByCategoryId(UUID categoryId, Pageable pageable);
    
    Page<Service> findByCategoryIdAndActive(UUID categoryId, Boolean active, Pageable pageable);
    
    List<Service> findByCategoryIdAndActiveOrderBySortOrderAsc(UUID categoryId, Boolean active);
    
    Page<Service> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE " +
           "(:categoryId IS NULL OR s.category.id = :categoryId) AND " +
           "(:active IS NULL OR s.active = :active) AND " +
           "(:name IS NULL OR :name = '' OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Service> findByFilters(
        @Param("categoryId") UUID categoryId,
        @Param("active") Boolean active,
        @Param("name") String name,
        Pageable pageable
    );
    
    long countByCategoryId(UUID categoryId);
}