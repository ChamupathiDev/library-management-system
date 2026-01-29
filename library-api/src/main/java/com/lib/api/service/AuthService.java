package com.lib.api.service;

import com.lib.api.dto.AuthResponse;
import com.lib.api.dto.LoginRequest;
import com.lib.api.dto.RegisterRequest;
import com.lib.api.dto.UserResponse;
import com.lib.api.model.Role;
import com.lib.api.model.User;
import com.lib.api.repository.UserRepository;
import com.lib.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // New injection

    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Generate Token
        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }
}
