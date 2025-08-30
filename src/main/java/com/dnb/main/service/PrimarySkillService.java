// src/main/java/com/dnb/main/service/PrimarySkillService.java
package com.dnb.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnb.main.model.PrimarySkill;
import com.dnb.main.model.User;
import com.dnb.main.repository.PrimarySkillRepository;

@Service
public class PrimarySkillService {
    
    @Autowired
    private PrimarySkillRepository primarySkillRepository;
    
    public List<PrimarySkill> getAllPrimarySkills() {
        return primarySkillRepository.findAll();
    }
    
    public Optional<PrimarySkill> getPrimarySkillById(Long id) {
        return primarySkillRepository.findById(id);
    }
    
    public Optional<PrimarySkill> getPrimarySkillByName(String name) {
        return primarySkillRepository.findByName(name);
    }
    
    public List<PrimarySkill> searchPrimarySkills(String keyword) {
        return primarySkillRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public PrimarySkill createPrimarySkill(PrimarySkill primarySkill, User createdBy) {
        primarySkill.setCreatedBy(createdBy);
        return primarySkillRepository.save(primarySkill);
    }
    
    public PrimarySkill updatePrimarySkill(Long id, PrimarySkill skillDetails) {
        return primarySkillRepository.findById(id).map(skill -> {
            skill.setName(skillDetails.getName());
            skill.setDescription(skillDetails.getDescription());
            return primarySkillRepository.save(skill);
        }).orElse(null);
    }
    
    public boolean deletePrimarySkill(Long id) {
        if (primarySkillRepository.existsById(id)) {
            primarySkillRepository.deleteById(id);
            return true;
        }
        return false;
    }
}