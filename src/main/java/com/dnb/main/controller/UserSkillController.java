// src/main/java/com/dnb/main/controller/UserSkillController.java
package com.dnb.main.controller;

import com.dnb.main.dto.ApiResponse;
import com.dnb.main.dto.UserSkillRequest;
import com.dnb.main.model.User;
import com.dnb.main.model.UserSkill;
import com.dnb.main.service.UserService;
import com.dnb.main.service.UserSkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-skills")
public class UserSkillController {
    
    @Autowired
    private UserSkillService userSkillService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserSkills(@PathVariable Long userId) {
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        
        List<UserSkill> skills = userSkillService.getUserSkills(user);
        return ResponseEntity.ok(ApiResponse.success("User skills retrieved", skills));
    }
    
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> addUserSkill(
            @PathVariable Long userId,
            @Valid @RequestBody UserSkillRequest userSkillRequest) {
        
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        
        // Check if the user already has this skill combination
        if (userSkillService.skillExistsForUser(
                user, userSkillRequest.getPrimarySkillId(), userSkillRequest.getSecondarySkillId())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("This skill combination already exists for the user"));
        }
        
        // Create user skill (in a real implementation, you would need to fetch the actual skill objects)
        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        // Note: In a real implementation, you would set primarySkill and secondarySkill objects
        // after fetching them from their respective services
        userSkill.setExpertiseLevel(userSkillRequest.getExpertiseLevel());
        userSkill.setYearsOfExperience(userSkillRequest.getYearsOfExperience());
        
        UserSkill savedSkill = userSkillService.addUserSkill(userSkill);
        return ResponseEntity.ok(ApiResponse.success("Skill added successfully", savedSkill));
    }
    
    @PutMapping("/{skillId}")
    public ResponseEntity<ApiResponse> updateUserSkill(
            @PathVariable Long skillId,
            @Valid @RequestBody UserSkillRequest userSkillRequest) {
        
        UserSkill updatedSkill = userSkillService.updateUserSkill(skillId, 
            // Create a UserSkill object from the request
            // In a real implementation, you would need to handle this properly
            new UserSkill() // This is a placeholder
        );
        
        if (updatedSkill == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Skill not found"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Skill updated successfully", updatedSkill));
    }
    
    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponse> deleteUserSkill(@PathVariable Long skillId) {
        boolean deleted = userSkillService.deleteUserSkill(skillId);
        if (!deleted) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Skill not found"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Skill deleted successfully"));
    }
}