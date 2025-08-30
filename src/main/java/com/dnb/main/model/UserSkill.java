// src/main/java/com/dnb/main/model/UserSkill.java
package com.dnb.main.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_skills")
@Data
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "primary_skill_id", nullable = false)
    private PrimarySkill primarySkill;
    
    @ManyToOne
    @JoinColumn(name = "secondary_skill_id", nullable = false)
    private SecondarySkill secondarySkill;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpertiseLevel expertiseLevel;
    
    @Column(nullable = false)
    private Integer yearsOfExperience;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum ExpertiseLevel {
        BEGINNER, INTERMEDIATE, EXPERT
    }
}