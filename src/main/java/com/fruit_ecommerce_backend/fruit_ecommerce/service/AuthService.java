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
    private final OtpService otpService;
    private final EmailService emailService;

    // STEP 1: Verify credentials and send OTP
    public String loginStepOne(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = otpService.generateOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), otp);

        return "OTP sent to registered email. Please verify.";
    }

    // STEP 2: Verify OTP and issue token
    public JwtResponse verifyOtpAndLogin(String email, String otp) {
        boolean isValid = otpService.validateOtp(email, otp);
        if (!isValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new JwtResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    // Existing signup (unchanged)
    public JwtResponse signup(SignupRequest request, Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role != null ? role : Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new JwtResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
