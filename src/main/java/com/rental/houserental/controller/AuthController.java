package com.rental.houserental.controller;

import com.rental.houserental.config.JwtUtil;
import com.rental.houserental.dto.LoginRequest;
import com.rental.houserental.dto.AuthResponse;
import com.rental.houserental.dto.RegisterRequest;
import com.rental.houserental.entity.Admin;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Tenant;
import com.rental.houserental.enums.Role;
import com.rental.houserental.repository.AdminRepository;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.name}")
    private String adminName;

    @Value("${app.admin.password}")
    private String adminPassword;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        //Check duplicate email in all tables
        if (tenantRepository.findByEmail(request.getEmail()).isPresent() ||
                adminRepository.findByEmail(request.getEmail()).isPresent() ||
                ownerRepository.findByEmail(request.getEmail()).isPresent()) {

            return ResponseEntity.badRequest()
                    .body("Email already exists!");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // ================= ADMIN AUTO CREATE =================
        if (adminRepository.findByEmail(adminEmail).isEmpty()) {

            Admin admin = new Admin();
            admin.setFullName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);

            adminRepository.save(admin);
        }

        // ================= OWNER REGISTER =================
        if (request.getRole() == Role.OWNER) {

            Owner owner = new Owner();
            owner.setFullName(request.getFullName());
            owner.setEmail(request.getEmail());
            owner.setPassword(encodedPassword);
            owner.setRole(Role.OWNER);

            ownerRepository.save(owner);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Owner Registered Successfully");
        }

        // ================= TENANT REGISTER =================
        Tenant tenant = new Tenant();
        tenant.setFullName(request.getFullName());
        tenant.setEmail(request.getEmail());
        tenant.setPassword(encodedPassword);
        tenant.setRole(Role.TENANT);

        tenantRepository.save(tenant);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Tenant Registered Successfully");
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Email or Password");
        }

        Long id=null;
        String email = request.getEmail();
        String fullName = null;
        String role = null;


        // Check Tenant
        Optional<Tenant> tenantOptional = tenantRepository.findByEmail(email);
        if (tenantOptional.isPresent()) {
            Tenant tenant = tenantOptional.get();
            id=tenant.getId();
            fullName = tenant.getFullName();
            role = tenant.getRole().name();
        }

        // Check Admin
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            id=admin.getId();
            fullName = admin.getFullName();
            role = admin.getRole().name();
        }

        // Check Owner
        Optional<Owner> ownerOptional = ownerRepository.findByEmail(email);
        if (ownerOptional.isPresent()) {
            Owner owner = ownerOptional.get();
            id=owner.getId();
            fullName = owner.getFullName();
            role = owner.getRole().name();

        }

        // Generate JWT
        String token = jwtUtil.generateToken(email);

        AuthResponse response = new AuthResponse(
                id,
                fullName,
                role,
                token,
                "Login Successful"
        );

        return ResponseEntity.ok(response);
    }
}