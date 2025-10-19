package com.fruit_ecommerce_backend.fruit_ecommerce.service;

import com.fruit_ecommerce_backend.fruit_ecommerce.dto.LoginRequest;
import com.fruit_ecommerce_backend.fruit_ecommerce.dto.SignupRequest;
import com.fruit_ecommerce_backend.fruit_ecommerce.dto.JwtResponse;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Role;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.User;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.UserRepository;
import com.fruit_ecommerce_backend.fruit_ecommerce.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public JwtResponse login(LoginRequest request) {
        //  1. Authenticate user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  2. Fetch user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  3. Generate token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        //  4. Return full user details
        return new JwtResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public JwtResponse signup(SignupRequest request, Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        // ✅ 1. Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role != null ? role : Role.USER)
                .build();

        userRepository.save(user);

        // ✅ 2. Generate token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        //  3. Return full user details so frontend can auto-login
        return new JwtResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
