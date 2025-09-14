package com.dnb.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnb.main.dto.ApiResponse;
import com.dnb.main.model.PrimarySkill;
import com.dnb.main.model.User;
import com.dnb.main.service.PrimarySkillService;
import com.dnb.main.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/primary-skills")
@CrossOrigin(origins = "http://localhost:3000") // allow React app to call this API
public class PrimarySkillController {

    @Autowired
    private PrimarySkillService primarySkillService;

    @Autowired
    private UserService userService;

    // Get all primary skills
    @GetMapping
    public ResponseEntity<List<PrimarySkill>> getAllPrimarySkills() {
        List<PrimarySkill> skills = primarySkillService.getAllPrimarySkills();
       // System.out.println("Retrieved skills: " + skills); // Debug log
        return ResponseEntity.ok(skills); // ✅ plain array
    }

    // Get skill by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPrimarySkillById(@PathVariable Long id) {
        Optional<PrimarySkill> skillOpt = primarySkillService.getPrimarySkillById(id);
        if (skillOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Primary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Primary skill retrieved", skillOpt.get()));
    }

    // Create a new skill for a specific user
    @PostMapping
    public ResponseEntity<ApiResponse> createPrimarySkill(
            @Valid @RequestBody PrimarySkill primarySkill,
            @RequestParam Long userId) {

        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
        }

        // Check if the skill already exists
        if (primarySkillService.getPrimarySkillByName(primarySkill.getName()).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Primary skill already exists"));
        }

        PrimarySkill createdSkill = primarySkillService.createPrimarySkill(primarySkill, userOpt.get());
        return ResponseEntity.ok(ApiResponse.success("Primary skill created", createdSkill));
    }

    // Update an existing skill
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePrimarySkill(
            @PathVariable Long id,
            @Valid @RequestBody PrimarySkill skillDetails) {

        PrimarySkill updatedSkill = primarySkillService.updatePrimarySkill(id, skillDetails);
        if (updatedSkill == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Primary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Primary skill updated", updatedSkill));
    }

    // Delete a skill by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePrimarySkill(@PathVariable Long id) {
        boolean deleted = primarySkillService.deletePrimarySkill(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Primary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Primary skill deleted"));
    }

    // Search skills by keyword
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchPrimarySkills(@RequestParam String keyword) {
        List<PrimarySkill> skills = primarySkillService.searchPrimarySkills(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results", skills));
    }
}
