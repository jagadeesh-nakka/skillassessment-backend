package com.dnb.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnb.main.model.PrimarySkill;
import com.dnb.main.model.SecondarySkill;

@Repository
public interface SecondarySkillRepository extends JpaRepository<SecondarySkill, Long> {

    // Get all secondary skills under a primary skill
    List<SecondarySkill> findByPrimarySkill(PrimarySkill primarySkill);

    // Search secondary skills by name (case-insensitive, partial match)
    List<SecondarySkill> findByNameContainingIgnoreCase(String name);

    // Option 1: Derived query (no need for @Query)
    Optional<SecondarySkill> findByNameAndPrimarySkill(String name, PrimarySkill primarySkill);

    // Option 2: Custom JPQL query (if needed)
    /*
    @Query("SELECT s FROM SecondarySkill s WHERE s.name = :name AND s.primarySkill = :primarySkill")
    Optional<SecondarySkill> findByNameAndPrimarySkill(
        @Param("name") String name, 
        @Param("primarySkill") PrimarySkill primarySkill);
    */
}
