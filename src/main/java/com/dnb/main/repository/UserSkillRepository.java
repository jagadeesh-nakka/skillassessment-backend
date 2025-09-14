// src/main/java/com/dnb/main/repository/UserSkillRepository.java
package com.dnb.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnb.main.model.User;
import com.dnb.main.model.UserSkill;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    // 🔹 Get all skills for a given user
    List<UserSkill> findByUser(User user);

    // 🔹 Check if a user already has a skill combination (traverses relationships)
    boolean existsByUserAndPrimarySkill_IdAndSecondarySkill_Id(
        User user, Long primarySkillId, Long secondarySkillId
    );

    
}
