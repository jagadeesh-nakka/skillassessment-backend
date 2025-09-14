// src/main/java/com/dnb/main/service/UserSkillService.java
package com.dnb.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnb.main.model.PrimarySkill;
import com.dnb.main.model.SecondarySkill;
import com.dnb.main.model.User;
import com.dnb.main.model.UserSkill;
import com.dnb.main.repository.PrimarySkillRepository;
import com.dnb.main.repository.SecondarySkillRepository;
import com.dnb.main.repository.UserSkillRepository;

@Service
public class UserSkillService {

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private PrimarySkillRepository primarySkillRepository;

    @Autowired
    private SecondarySkillRepository secondarySkillRepository;

    // 🔹 Fetch all skills of a user
    public List<UserSkill> getUserSkills(User user) {
        return userSkillRepository.findByUser(user);
    }

    // 🔹 Add new user skill
    public UserSkill addUserSkill(UserSkill userSkill) {
        return userSkillRepository.save(userSkill);
    }

    // 🔹 Update existing user skill
    public UserSkill updateUserSkill(Long id, UserSkill skillDetails) {
        return userSkillRepository.findById(id).map(skill -> {
            skill.setPrimarySkill(skillDetails.getPrimarySkill());
            skill.setSecondarySkill(skillDetails.getSecondarySkill());
            skill.setExpertiseLevel(skillDetails.getExpertiseLevel());
            skill.setYearsOfExperience(skillDetails.getYearsOfExperience());
            return userSkillRepository.save(skill);
        }).orElse(null);
    }

    // 🔹 Delete user skill
    public boolean deleteUserSkill(Long id) {
        if (userSkillRepository.existsById(id)) {
            userSkillRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 🔹 Check if skill combination already exists for a user
    public boolean skillExistsForUser(User user, Long primarySkillId, Long secondarySkillId) {
        return userSkillRepository.existsByUserAndPrimarySkill_IdAndSecondarySkill_Id(
                user, primarySkillId, secondarySkillId
        );
    }

    // 🔹 Fetch PrimarySkill by ID
    public Optional<PrimarySkill> getPrimarySkillById(Long id) {
        return primarySkillRepository.findById(id);
    }

    // 🔹 Fetch SecondarySkill by ID
    public Optional<SecondarySkill> getSecondarySkillById(Long id) {
        return secondarySkillRepository.findById(id);
    }
}
