package com.example.librarysystem.repository;

import com.example.librarysystem.entity.PurchaseRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRecommendationRepository extends JpaRepository<PurchaseRecommendation, Long> {
    List<PurchaseRecommendation> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<PurchaseRecommendation> findByStatusOrderByCreatedAtDesc(String status);
    List<PurchaseRecommendation> findAllByOrderByCreatedAtDesc();
    long countByStatus(String status);
}
