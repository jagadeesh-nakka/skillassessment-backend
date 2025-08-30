// src/main/java/com/dnb/main/repository/PrimarySkillRepository.java
package com.dnb.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnb.main.model.PrimarySkill;

@Repository
public interface PrimarySkillRepository extends JpaRepository<PrimarySkill, Long> {
    Optional<PrimarySkill> findByName(String name);
    List<PrimarySkill> findByNameContainingIgnoreCase(String name);
}