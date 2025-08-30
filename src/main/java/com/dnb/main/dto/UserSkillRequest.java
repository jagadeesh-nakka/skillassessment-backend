// src/main/java/com/dnb/main/dto/UserSkillRequest.java
package com.dnb.main.dto;

import com.dnb.main.model.UserSkill;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSkillRequest {
    @NotNull(message = "Primary skill ID is required")
    private Long primarySkillId;
    
    @NotNull(message = "Secondary skill ID is required")
    private Long secondarySkillId;
    
    @NotNull(message = "Expertise level is required")
    private UserSkill.ExpertiseLevel expertiseLevel;
    
    @NotNull(message = "Years of experience is required")
    private Integer yearsOfExperience;
}