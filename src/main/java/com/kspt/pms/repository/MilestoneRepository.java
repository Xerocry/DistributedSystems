package com.kspt.pms.repository;

import com.kspt.pms.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by kivi on 03.12.17.
 */
@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    Optional<Milestone> findById(Long id);
    Collection<Milestone> findByProjectName(String name);
}
