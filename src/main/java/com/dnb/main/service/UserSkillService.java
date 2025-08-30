// src/main/java/com/dnb/main/service/UserSkillService.java
package com.dnb.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnb.main.model.User;
import com.dnb.main.model.UserSkill;
import com.dnb.main.repository.UserSkillRepository;

@Service
public class UserSkillService {
    
    @Autowired
    private UserSkillRepository userSkillRepository;
    
    public List<UserSkill> getUserSkills(User user) {
        return userSkillRepository.findByUser(user);
    }
    
    public UserSkill addUserSkill(UserSkill userSkill) {
        return userSkillRepository.save(userSkill);
    }
    
    public UserSkill updateUserSkill(Long id, UserSkill skillDetails) {
        return userSkillRepository.findById(id).map(skill -> {
            skill.setExpertiseLevel(skillDetails.getExpertiseLevel());
            skill.setYearsOfExperience(skillDetails.getYearsOfExperience());
            return userSkillRepository.save(skill);
        }).orElse(null);
    }
    
    public boolean deleteUserSkill(Long id) {
        if (userSkillRepository.existsById(id)) {
            userSkillRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean skillExistsForUser(User user, Long primarySkillId, Long secondarySkillId) {
        return userSkillRepository.existsByUserAndPrimarySkillIdAndSecondarySkillId(
            user, primarySkillId, secondarySkillId);
    }
}