// // src/main/java/com/dnb/main/controller/UserSkillController.java
// package com.dnb.main.controller;

// import com.dnb.main.dto.ApiResponse;
// import com.dnb.main.dto.UserSkillRequest;
// import com.dnb.main.model.User;
// import com.dnb.main.model.UserSkill;
// import com.dnb.main.service.UserService;
// import com.dnb.main.service.UserSkillService;
// import jakarta.validation.Valid;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/user-skills")
// public class UserSkillController {
    
//     @Autowired
//     private UserSkillService userSkillService;
    
//     @Autowired
//     private UserService userService;
    
//     @GetMapping("/{userId}")
//     public ResponseEntity<ApiResponse> getUserSkills(@PathVariable Long userId) {
//         User user = userService.getUserById(userId).orElse(null);
//         if (user == null) {
//             return ResponseEntity.badRequest()
//                     .body(ApiResponse.error("User not found"));
//         }
        
//         List<UserSkill> skills = userSkillService.getUserSkills(user);
//         return ResponseEntity.ok(ApiResponse.success("User skills retrieved", skills));
//     }
    
//     @PostMapping("/{userId}")
//     public ResponseEntity<ApiResponse> addUserSkill(
//             @PathVariable Long userId,
//             @Valid @RequestBody UserSkillRequest userSkillRequest) {
        
//         User user = userService.getUserById(userId).orElse(null);
//         if (user == null) {
//             return ResponseEntity.badRequest()
//                     .body(ApiResponse.error("User not found"));
//         }
        
//         // Check if the user already has this skill combination
//         if (userSkillService.skillExistsForUser(
//                 user, userSkillRequest.getPrimarySkillId(), userSkillRequest.getSecondarySkillId())) {
//             return ResponseEntity.badRequest()
//                     .body(ApiResponse.error("This skill combination already exists for the user"));
//         }
        
//         // Create user skill (in a real implementation, you would need to fetch the actual skill objects)
//         UserSkill userSkill = new UserSkill();
//         userSkill.setUser(user);
//         // Note: In a real implementation, you would set primarySkill and secondarySkill objects
//         // after fetching them from their respective services
//         userSkill.setExpertiseLevel(userSkillRequest.getExpertiseLevel());
//         userSkill.setYearsOfExperience(userSkillRequest.getYearsOfExperience());
        
//         UserSkill savedSkill = userSkillService.addUserSkill(userSkill);
//         return ResponseEntity.ok(ApiResponse.success("Skill added successfully", savedSkill));
//     }
    
//     @PutMapping("/{skillId}")
//     public ResponseEntity<ApiResponse> updateUserSkill(
//             @PathVariable Long skillId,
//             @Valid @RequestBody UserSkillRequest userSkillRequest) {
        
//         UserSkill updatedSkill = userSkillService.updateUserSkill(skillId, 
//             // Create a UserSkill object from the request
//             // In a real implementation, you would need to handle this properly
//             new UserSkill() // This is a placeholder
//         );
        
//         if (updatedSkill == null) {
//             return ResponseEntity.badRequest()
//                     .body(ApiResponse.error("Skill not found"));
//         }
        
//         return ResponseEntity.ok(ApiResponse.success("Skill updated successfully", updatedSkill));
//     }
    
//     @DeleteMapping("/{skillId}")
//     public ResponseEntity<ApiResponse> deleteUserSkill(@PathVariable Long skillId) {
//         boolean deleted = userSkillService.deleteUserSkill(skillId);
//         if (!deleted) {
//             return ResponseEntity.badRequest()
//                     .body(ApiResponse.error("Skill not found"));
//         }
        
//         return ResponseEntity.ok(ApiResponse.success("Skill deleted successfully"));
//     }
// }


// src/main/java/com/dnb/main/controller/UserSkillController.java
package com.dnb.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dnb.main.dto.ApiResponse;
import com.dnb.main.dto.UserSkillRequest;
import com.dnb.main.model.PrimarySkill;
import com.dnb.main.model.SecondarySkill;
import com.dnb.main.model.User;
import com.dnb.main.model.UserSkill;
import com.dnb.main.service.UserService;
import com.dnb.main.service.UserSkillService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user-skills")
public class UserSkillController {

    @Autowired
    private UserSkillService userSkillService;

    @Autowired
    private UserService userService;

    // Get user skills
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserSkills(@PathVariable Long userId) {
        User user = userService.getUserById(userId).orElse(null);
        System.err.println("User fetched: " + user); // Debug log
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }
        List<UserSkill> skills = userSkillService.getUserSkills(user);
        return ResponseEntity.ok(ApiResponse.success("User skills retrieved", skills));
    }

    // Add a single skill (optional)
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> addUserSkill(
            @PathVariable Long userId,
            @Valid @RequestBody UserSkillRequest userSkillRequest) {

        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("User not found"));
        }

        PrimarySkill primarySkill = userSkillService.getPrimarySkillById(userSkillRequest.getPrimarySkillId())
                .orElse(null);
        SecondarySkill secondarySkill = userSkillService.getSecondarySkillById(userSkillRequest.getSecondarySkillId())
                .orElse(null);

        if (primarySkill == null || (userSkillRequest.getSecondarySkillId() != null && secondarySkill == null)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid skill IDs"));
        }

        // Skip duplicates
        if (userSkillService.skillExistsForUser(user,
                userSkillRequest.getPrimarySkillId(),
                userSkillRequest.getSecondarySkillId())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("This skill combination already exists"));
        }

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setPrimarySkill(primarySkill);
        userSkill.setSecondarySkill(secondarySkill);
        userSkill.setExpertiseLevel(userSkillRequest.getExpertiseLevel());
        userSkill.setYearsOfExperience(userSkillRequest.getYearsOfExperience());

        UserSkill savedSkill = userSkillService.addUserSkill(userSkill);
        return ResponseEntity.ok(ApiResponse.success("Skill added successfully", savedSkill));
    }

    // Bulk skill submission
    @PostMapping("/{userId}/bulk")
    public ResponseEntity<ApiResponse> addUserSkillsBulk(
            @PathVariable Long userId,
            @Valid @RequestBody List<UserSkillRequest> userSkillRequests) {

        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
        }

        List<UserSkill> savedSkills = new ArrayList<>();

        for (UserSkillRequest req : userSkillRequests) {
            if (userSkillService.skillExistsForUser(user, req.getPrimarySkillId(), req.getSecondarySkillId())) {
                continue; // skip duplicates
            }

            PrimarySkill primarySkill = userSkillService.getPrimarySkillById(req.getPrimarySkillId()).orElse(null);
            SecondarySkill secondarySkill = null;
            if (req.getSecondarySkillId() != null) {
                secondarySkill = userSkillService.getSecondarySkillById(req.getSecondarySkillId()).orElse(null);
            }

            if (primarySkill == null) continue; // skip invalid primary skill
            if (req.getSecondarySkillId() != null && secondarySkill == null) continue; // skip invalid secondary

            UserSkill userSkill = new UserSkill();
            userSkill.setUser(user);
            userSkill.setPrimarySkill(primarySkill);
            userSkill.setSecondarySkill(secondarySkill);
            userSkill.setExpertiseLevel(req.getExpertiseLevel());
            userSkill.setYearsOfExperience(req.getYearsOfExperience());

            savedSkills.add(userSkillService.addUserSkill(userSkill));
        }

        return ResponseEntity.ok(ApiResponse.success("Skills added successfully", savedSkills));
    }

    // Update a skill
    @PutMapping("/{skillId}")
    public ResponseEntity<ApiResponse> updateUserSkill(
            @PathVariable Long skillId,
            @Valid @RequestBody UserSkillRequest userSkillRequest) {

        PrimarySkill primarySkill = userSkillService.getPrimarySkillById(userSkillRequest.getPrimarySkillId())
                .orElse(null);
        SecondarySkill secondarySkill = userSkillService.getSecondarySkillById(userSkillRequest.getSecondarySkillId())
                .orElse(null);

        if (primarySkill == null || (userSkillRequest.getSecondarySkillId() != null && secondarySkill == null)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid skill IDs"));
        }

        UserSkill updatedSkillObj = new UserSkill();
        updatedSkillObj.setPrimarySkill(primarySkill);
        updatedSkillObj.setSecondarySkill(secondarySkill);
        updatedSkillObj.setExpertiseLevel(userSkillRequest.getExpertiseLevel());
        updatedSkillObj.setYearsOfExperience(userSkillRequest.getYearsOfExperience());

        UserSkill updatedSkill = userSkillService.updateUserSkill(skillId, updatedSkillObj);
        if (updatedSkill == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Skill not found"));
        }

        return ResponseEntity.ok(ApiResponse.success("Skill updated successfully", updatedSkill));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<ApiResponse> deleteUserSkill(@PathVariable Long skillId) {
        boolean deleted = userSkillService.deleteUserSkill(skillId);
        if (!deleted) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Skill not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("Skill deleted successfully"));
    }
}
