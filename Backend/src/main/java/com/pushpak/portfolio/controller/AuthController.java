package com.pushpak.portfolio.controller;

import com.pushpak.portfolio.config.JwtUtil;
import com.pushpak.portfolio.dto.LoginRequest;
import com.pushpak.portfolio.dto.LoginResponse;
import com.pushpak.portfolio.repository.AdminRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return adminRepository.findByUsername(req.getUsername())
                .filter(admin -> passwordEncoder.matches(req.getPassword(), admin.getPassword()))
                .<ResponseEntity<?>>map(admin -> ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(admin.getUsername()))))
                .orElseGet(() -> ResponseEntity.status(401).body("Invalid username or password"));
    }
}
