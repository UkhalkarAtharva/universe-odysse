package com.universeodyssey.universe_odyssey.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private String email;
    private String fullName;
    
    public AuthResponse() {
    }
    
    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public AuthResponse(boolean success, String message, String email, String fullName) {
        this.success = success;
        this.message = message;
        this.email = email;
        this.fullName = fullName;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
