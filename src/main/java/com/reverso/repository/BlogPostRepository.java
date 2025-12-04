package com.reverso.repository;

import com.reverso.model.BlogPost;
import com.reverso.model.enums.BlogPostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, UUID> {

    List<BlogPost> findByStatus(BlogPostStatus status);

    List<BlogPost> findByCategoryIgnoreCase(String category);

    List<BlogPost> findByStatusAndCategoryIgnoreCase(BlogPostStatus status, String category);

    List<BlogPost> findTop5ByStatusOrderByPublishedAtDesc(BlogPostStatus status);
}
