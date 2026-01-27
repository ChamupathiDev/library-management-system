package com.lib.api.service;

import com.lib.api.dto.RegisterRequest;
import com.lib.api.dto.UserResponse;
import com.lib.api.model.Role;
import com.lib.api.model.User;
import com.lib.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // This is the "Injection"

    public UserResponse registerUser(RegisterRequest request) {
        // 1. Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            // This will now be caught by your GlobalExceptionHandler
            throw new RuntimeException("User already exists with email: " + request.getEmail());
        }

        // 2. Create and map the new User entity
        User user = new User();
        user.setEmail(request.getEmail());

        // ENCRYPTION HAPPENS HERE:
        // We take the plain text from the request and encode it before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        // 3. Save to database
        User savedUser = userRepository.save(user);

        // 4. Return the safe Response DTO
        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());

        return response;
    }
}
