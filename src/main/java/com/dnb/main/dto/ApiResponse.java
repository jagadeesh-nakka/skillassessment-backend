// src/main/java/com/dnb/main/dto/ApiResponse.java
package com.dnb.main.dto;

import lombok.Data;

@Data
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;
    
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }
    
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }
    
    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }
}