// src/main/java/com/dnb/main/controller/AdminController.java
package com.dnb.main.controller;

import com.dnb.main.dto.ApiResponse;
import com.dnb.main.dto.UserSkillExportDTO;
import com.dnb.main.model.PrimarySkill;
import com.dnb.main.model.SecondarySkill;
import com.dnb.main.model.User;
import com.dnb.main.model.UserSkill;
import com.dnb.main.service.PrimarySkillService;
import com.dnb.main.service.SecondarySkillService;
import com.dnb.main.service.UserService;
import com.dnb.main.service.UserSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PrimarySkillService primarySkillService;
    
    @Autowired
    private SecondarySkillService secondarySkillService;
    
    @Autowired
    private UserSkillService userSkillService;
    
    // User Management Endpoints
    
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved", users));
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("User retrieved", user.get()));
    }
    
    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse> updateUserRole(
            @PathVariable Long id,
            @RequestParam User.Role role) {
        
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        
        User user = userOptional.get();
        user.setRole(role);
        User updatedUser = userService.updateUser(id, user);
        
        return ResponseEntity.ok(ApiResponse.success("User role updated", updatedUser));
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
    
    // Primary Skills Management Endpoints
    
    @PostMapping("/primary-skills")
    public ResponseEntity<ApiResponse> createPrimarySkill(
            @RequestBody PrimarySkill primarySkill,
            @RequestParam Long adminId) {
        
        User admin = userService.getUserById(adminId).orElse(null);
        if (admin == null || admin.getRole() != User.Role.ADMIN) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Admin user not found"));
        }
        
        // Check if skill already exists
        if (primarySkillService.getPrimarySkillByName(primarySkill.getName()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Primary skill already exists"));
        }
        
        PrimarySkill createdSkill = primarySkillService.createPrimarySkill(primarySkill, admin);
        return ResponseEntity.ok(ApiResponse.success("Primary skill created", createdSkill));
    }
    
    @PutMapping("/primary-skills/{id}")
    public ResponseEntity<ApiResponse> updatePrimarySkill(
            @PathVariable Long id,
            @RequestBody PrimarySkill skillDetails) {
        
        PrimarySkill updatedSkill = primarySkillService.updatePrimarySkill(id, skillDetails);
        if (updatedSkill == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Primary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Primary skill updated", updatedSkill));
    }
    
    @DeleteMapping("/primary-skills/{id}")
    public ResponseEntity<ApiResponse> deletePrimarySkill(@PathVariable Long id) {
        boolean deleted = primarySkillService.deletePrimarySkill(id);
        if (!deleted) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Primary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Primary skill deleted"));
    }
    
    // Secondary Skills Management Endpoints
    
    @PostMapping("/secondary-skills")
    public ResponseEntity<ApiResponse> createSecondarySkill(
            @RequestBody SecondarySkill secondarySkill,
            @RequestParam Long adminId) {
        
        User admin = userService.getUserById(adminId).orElse(null);
        if (admin == null || admin.getRole() != User.Role.ADMIN) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Admin user not found"));
        }
        
        // Check if primary skill exists
        if (secondarySkill.getPrimarySkill() == null || 
            secondarySkill.getPrimarySkill().getId() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Primary skill is required"));
        }
        
        PrimarySkill primarySkill = primarySkillService.getPrimarySkillById(
                secondarySkill.getPrimarySkill().getId()).orElse(null);
        if (primarySkill == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Primary skill not found"));
        }
        
        // Check if skill already exists for this primary skill
        if (secondarySkillService.getSecondarySkillByNameAndPrimarySkill(
                secondarySkill.getName(), primarySkill).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Secondary skill already exists for this primary skill"));
        }
        
        SecondarySkill createdSkill = secondarySkillService.createSecondarySkill(secondarySkill, admin);
        return ResponseEntity.ok(ApiResponse.success("Secondary skill created", createdSkill));
    }
    
    @PutMapping("/secondary-skills/{id}")
    public ResponseEntity<ApiResponse> updateSecondarySkill(
            @PathVariable Long id,
            @RequestBody SecondarySkill skillDetails) {
        
        SecondarySkill updatedSkill = secondarySkillService.updateSecondarySkill(id, skillDetails);
        if (updatedSkill == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Secondary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Secondary skill updated", updatedSkill));
    }
    
    @DeleteMapping("/secondary-skills/{id}")
    public ResponseEntity<ApiResponse> deleteSecondarySkill(@PathVariable Long id) {
        boolean deleted = secondarySkillService.deleteSecondarySkill(id);
        if (!deleted) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Secondary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Secondary skill deleted"));
    }
    
    // Analytics and Reporting Endpoints
    
    @GetMapping("/analytics/user-skills")
    public ResponseEntity<ApiResponse> getUserSkillsAnalytics() {
        List<User> users = userService.getAllUsers();
        List<UserSkillExportDTO> analyticsData = users.stream()
                .flatMap(user -> userSkillService.getUserSkills(user).stream()
                        .map(skill -> convertToExportDTO(user, skill)))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success("User skills analytics", analyticsData));
    }
    
    @GetMapping("/analytics/skill-distribution")
    public ResponseEntity<ApiResponse> getSkillDistribution() {
        List<UserSkillExportDTO> analyticsData = userService.getAllUsers().stream()
                .flatMap(user -> userSkillService.getUserSkills(user).stream()
                        .map(skill -> convertToExportDTO(user, skill)))
                .collect(Collectors.toList());
        
        // You can add more specific analytics logic here
        return ResponseEntity.ok(ApiResponse.success("Skill distribution analytics", analyticsData));
    }
    
    @GetMapping("/analytics/expertise-levels")
    public ResponseEntity<ApiResponse> getExpertiseLevelDistribution() {
        List<UserSkillExportDTO> analyticsData = userService.getAllUsers().stream()
                .flatMap(user -> userSkillService.getUserSkills(user).stream()
                        .map(skill -> convertToExportDTO(user, skill)))
                .collect(Collectors.toList());
        
        // You can add more specific analytics logic here
        return ResponseEntity.ok(ApiResponse.success("Expertise level distribution", analyticsData));
    }
    
    @GetMapping("/export/user-skills")
    public ResponseEntity<List<UserSkillExportDTO>> exportUserSkills() {
        List<UserSkillExportDTO> exportData = userService.getAllUsers().stream()
                .flatMap(user -> userSkillService.getUserSkills(user).stream()
                        .map(skill -> convertToExportDTO(user, skill)))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(exportData);
    }
    
    // Helper method to convert UserSkill to Export DTO
    private UserSkillExportDTO convertToExportDTO(User user, UserSkill userSkill) {
        UserSkillExportDTO dto = new UserSkillExportDTO();
        dto.setUserId(user.getId());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        
        if (userSkill.getPrimarySkill() != null) {
            dto.setPrimarySkill(userSkill.getPrimarySkill().getName());
        }
        
        if (userSkill.getSecondarySkill() != null) {
            dto.setSecondarySkill(userSkill.getSecondarySkill().getName());
        }
        
        dto.setExpertiseLevel(userSkill.getExpertiseLevel().name());
        dto.setYearsOfExperience(userSkill.getYearsOfExperience());
        
        return dto;
    }
}