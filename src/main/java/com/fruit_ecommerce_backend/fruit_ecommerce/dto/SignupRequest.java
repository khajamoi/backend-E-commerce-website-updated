package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
    private String phoneNumber;

    @NotBlank
    private String password;
    private Role role; 
}
