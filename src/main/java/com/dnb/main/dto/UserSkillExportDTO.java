// src/main/java/com/dnb/main/dto/UserSkillExportDTO.java
package com.dnb.main.dto;

import lombok.Data;

@Data
public class UserSkillExportDTO {
    private Long userId;
    private String employeeId;
    private String username;
    private String name;
    private String email;
    private String primarySkill;
    private String secondarySkill;
    private String expertiseLevel;
    private Integer yearsOfExperience;
}