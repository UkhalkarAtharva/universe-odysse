package com.universeodyssey.universe_odyssey.service;

import com.universeodyssey.universe_odyssey.dto.LoginRequest;
import com.universeodyssey.universe_odyssey.dto.SignupRequest;
import com.universeodyssey.universe_odyssey.model.User;
import com.universeodyssey.universe_odyssey.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final int MIN_PASSWORD_LENGTH = 8;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Register a new user
     * 
     * @param request SignupRequest containing email, password, fullName
     * @return User if registration successful
     * @throws IllegalArgumentException if validation fails
     */
    public User registerUser(SignupRequest request) {
        // Validate inputs
        validateSignupRequest(request);

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create new user with hashed password
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Ensure username/role/active match DB expectations (table "users" contains username and role)
        user.setUsername(request.getEmail());
        user.setRole("USER");
        user.setActive(true);

        return userRepository.save(user);
    }
    
    /**
     * Authenticate user with email and password
     * 
     * @param request LoginRequest containing email and password
     * @return User if authentication successful
     * @throws IllegalArgumentException if authentication fails
     */
    public User authenticateUser(LoginRequest request) {
        // Validate inputs
        validateLoginRequest(request);
        
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        
        return user.get();
    }
    
    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Validate signup request
     */
    private void validateSignupRequest(SignupRequest request) {
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (!isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        
        if (request.getConfirmPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }
    
    /**
     * Validate login request
     */
    private void validateLoginRequest(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
