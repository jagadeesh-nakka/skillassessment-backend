// src/main/java/com/dnb/main/controller/SecondarySkillController.java
package com.dnb.main.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.dnb.main.model.SecondarySkill;
import com.dnb.main.model.User;
import com.dnb.main.service.PrimarySkillService;
import com.dnb.main.service.SecondarySkillService;
import com.dnb.main.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secondary-skills")
public class SecondarySkillController {

    @Autowired
    private SecondarySkillService secondarySkillService;

    @Autowired
    private PrimarySkillService primarySkillService;

    @Autowired
    private UserService userService;

    // @GetMapping
    // public ResponseEntity<ApiResponse> getSecondarySkillsByPrimarySkill(
    //         @RequestParam Long primarySkillId) {

    //     PrimarySkill primarySkill = primarySkillService.getPrimarySkillById(primarySkillId).orElse(null);
    //     if (primarySkill == null) {
    //         return ResponseEntity.badRequest()
    //                 .body(ApiResponse.error("Primary skill not found"));
    //     }

    //     List<SecondarySkill> skills = secondarySkillService.getSecondarySkillsByPrimarySkill(primarySkill);
    //     return ResponseEntity.ok(ApiResponse.success("Secondary skills retrieved", skills));
    // }

 @GetMapping
public ResponseEntity<ApiResponse> getSecondarySkillsByPrimarySkill(
        @RequestParam(name = "primarySkillId", required = false) Long primarySkillId) {

    if (primarySkillId == null) {
        return ResponseEntity.ok(ApiResponse.success("Secondary skills retrieved", Collections.emptyList()));
    }

    List<SecondarySkill> skills = primarySkillService.getPrimarySkillById(primarySkillId)
            .map(primarySkill -> secondarySkillService.getSecondarySkillsByPrimarySkill(primarySkill))
            .orElse(Collections.emptyList());

    return ResponseEntity.ok(ApiResponse.success("Secondary skills retrieved", skills));
}


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSecondarySkillById(@PathVariable Long id) {
        Optional<SecondarySkill> skill = secondarySkillService.getSecondarySkillById(id);
        if (skill.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Secondary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Secondary skill retrieved", skill.get()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSecondarySkill(
            @Valid @RequestBody SecondarySkill secondarySkill,
            @RequestParam Long userId) {

        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }

        // Check if primary skill exists
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

        SecondarySkill createdSkill = secondarySkillService.createSecondarySkill(secondarySkill, user);
        return ResponseEntity.ok(ApiResponse.success("Secondary skill created", createdSkill));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSecondarySkill(
            @PathVariable Long id,
            @Valid @RequestBody SecondarySkill skillDetails) {

        SecondarySkill updatedSkill = secondarySkillService.updateSecondarySkill(id, skillDetails);
        if (updatedSkill == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Secondary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Secondary skill updated", updatedSkill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSecondarySkill(@PathVariable Long id) {
        boolean deleted = secondarySkillService.deleteSecondarySkill(id);
        if (!deleted) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Secondary skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Secondary skill deleted"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchSecondarySkills(@RequestParam String keyword) {
        List<SecondarySkill> skills = secondarySkillService.searchSecondarySkills(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results", skills));
    }
}
