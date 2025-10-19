package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import com.fruit_ecommerce_backend.fruit_ecommerce.dto.LoginRequest;
import com.fruit_ecommerce_backend.fruit_ecommerce.dto.SignupRequest;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Role;
import com.fruit_ecommerce_backend.fruit_ecommerce.dto.JwtResponse;
import com.fruit_ecommerce_backend.fruit_ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginStepOne(request)); // Step 1: send OTP
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<JwtResponse> verifyOtp(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String otp = req.get("otp");
        return ResponseEntity.ok(authService.verifyOtpAndLogin(email, otp)); // Step 2: verify OTP + issue token
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request, Role.USER));
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<JwtResponse> signupAdmin(@Valid @RequestBody SignupRequest request) {
        Role role = request.getRole() != null ? request.getRole() : Role.ADMIN;
        return ResponseEntity.ok(authService.signup(request, role));
    }
}


//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//    private final AuthService authService;
//
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
//        return ResponseEntity.ok(authService.login(request));
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<JwtResponse> signup(@Valid @RequestBody SignupRequest request) {
//        return ResponseEntity.ok(authService.signup(request, Role.USER));
//    }
//
//    @PostMapping("/admin/signup")
//    public ResponseEntity<JwtResponse> signupAdmin(@Valid @RequestBody SignupRequest request) {
//    Role role = request.getRole() != null ? request.getRole() : Role.ADMIN;
//    return ResponseEntity.ok(authService.signup(request, role));
//    }}
