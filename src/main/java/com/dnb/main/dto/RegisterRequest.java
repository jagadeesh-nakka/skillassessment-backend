// src/main/java/com/dnb/main/dto/RegisterRequest.java
package com.dnb.main.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Employee ID is required")
    @Pattern(regexp = "^\\d+$", message = "Employee ID must contain only numbers")
    private String employeeId;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+@dnb\\.com$", 
             message = "Email must be a valid DNB email address")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}