//package com.fruit_ecommerce_backend.fruit_ecommerce.service;
//
//import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Role;
//import com.fruit_ecommerce_backend.fruit_ecommerce.entity.User;
//import com.fruit_ecommerce_backend.fruit_ecommerce.repository.RoleRepository;
//import com.fruit_ecommerce_backend.fruit_ecommerce.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import jakarta.annotation.PostConstruct;
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class UserService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @PostConstruct
//    public void initDefaultRolesAndAdmin() {
//        // Ensure default roles exist
//        roleRepository.findByName("ROLE_USER")
//                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));
//        roleRepository.findByName("ROLE_ADMIN")
//                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));
//
//        // Create an admin if none exists
//        if (!userRepository.existsByEmail("admin@fruitstore.com")) {
//            User admin = new User();
//            admin.setName("Admin");
//            admin.setEmail("admin@fruitstore.com");
//            admin.setPassword(passwordEncoder.encode("admin123"));
//            Set<Role> rs = new HashSet<>();
//            roleRepository.findByName("ROLE_ADMIN").ifPresent(rs::add);
//            roleRepository.findByName("ROLE_USER").ifPresent(rs::add);
//            admin.setRoles(rs);
//            userRepository.save(admin);
//        }
//    }
//
//    public User register(String name, String email, String rawPassword) {
//        if (userRepository.existsByEmail(email)) {
//            throw new RuntimeException("Email already in use");
//        }
//        User u = new User();
//        u.setName(name);
//        u.setEmail(email);
//        u.setPassword(passwordEncoder.encode(rawPassword));
//        Set<Role> roles = new HashSet<>();
//        roleRepository.findByName("ROLE_USER").ifPresent(roles::add);
//        u.setRoles(roles);
//        return userRepository.save(u);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//    }
//}
