package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
	@GetMapping("/login")
	public String adminLoginPage() {
	// resolves to src/main/resources/static/admin-login.html
	return "forward:/admin-login.html";
	}


	@GetMapping("/register")
	public String adminRegisterPage() {
	// resolves to src/main/resources/static/admin-register.html
	return "forward:/admin-register.html";
	}
}
