// src/main/java/com/dnb/main/repository/UserSkillRepository.java
package com.dnb.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnb.main.model.User;
import com.dnb.main.model.UserSkill;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUser(User user);
    Boolean existsByUserAndPrimarySkillIdAndSecondarySkillId(
        User user, Long primarySkillId, Long secondarySkillId);
}