package com.gscdz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gscdz.model.KnowledgePoint;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
public interface KnowledgePointRepository extends JpaRepository<KnowledgePoint, Long>{
    public Optional<KnowledgePoint> findKnowledgePointByTitle(String title);
    public void deleteKnowledgePointById(Long id);
}
