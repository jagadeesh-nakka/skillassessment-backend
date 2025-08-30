package com.dnb.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnb.main.model.PrimarySkill;
import com.dnb.main.model.SecondarySkill;
import com.dnb.main.model.User;
import com.dnb.main.repository.SecondarySkillRepository;

@Service
public class SecondarySkillService {

    @Autowired
    private SecondarySkillRepository secondarySkillRepository;

    // Get all secondary skills
    public List<SecondarySkill> getAllSecondarySkills() {
        return secondarySkillRepository.findAll();
    }

    // Get secondary skill by ID
    public Optional<SecondarySkill> getSecondarySkillById(Long id) {
        return secondarySkillRepository.findById(id);
    }

    // Get secondary skills for a given primary skill
    public List<SecondarySkill> getSecondarySkillsByPrimarySkill(PrimarySkill primarySkill) {
        return secondarySkillRepository.findByPrimarySkill(primarySkill);
    }

    // Search secondary skills by keyword
    public List<SecondarySkill> searchSecondarySkills(String keyword) {
        return secondarySkillRepository.findByNameContainingIgnoreCase(keyword);
    }

    // Create a new secondary skill
    public SecondarySkill createSecondarySkill(SecondarySkill secondarySkill, User createdBy) {
        secondarySkill.setCreatedBy(createdBy);
        return secondarySkillRepository.save(secondarySkill);
    }

    // Update existing secondary skill
    public SecondarySkill updateSecondarySkill(Long id, SecondarySkill skillDetails) {
        return secondarySkillRepository.findById(id).map(skill -> {
            skill.setName(skillDetails.getName());
            skill.setPrimarySkill(skillDetails.getPrimarySkill());
            return secondarySkillRepository.save(skill);
        }).orElse(null);
    }

    // Delete secondary skill by ID
    public boolean deleteSecondarySkill(Long id) {
        if (secondarySkillRepository.existsById(id)) {
            secondarySkillRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Check if a secondary skill exists for a primary skill
    public Optional<SecondarySkill> getSecondarySkillByNameAndPrimarySkill(String name, PrimarySkill primarySkill) {
        return secondarySkillRepository.findByNameAndPrimarySkill(name, primarySkill);
    }
}
