package com.rental.houserental.service;

import com.rental.houserental.config.JwtUtil;
import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.entity.Admin;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Tenant;
import com.rental.houserental.repository.AdminRepository;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // =========================
    // LOGIN METHOD (Spring Security)
    // =========================
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Check Tenant
        Optional<Tenant> tenantOptional = tenantRepository.findByEmail(email);
        if (tenantOptional.isPresent()) {
            Tenant tenant = tenantOptional.get();
            return buildUserDetails(
                    tenant.getEmail(),
                    tenant.getPassword(),
                    tenant.getRole().name()
            );
        }

        // Check Admin
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return buildUserDetails(
                    admin.getEmail(),
                    admin.getPassword(),
                    admin.getRole().name()
            );
        }

        // Check Owner
        Optional<Owner> ownerOptional = ownerRepository.findByEmail(email);
        if (ownerOptional.isPresent()) {
            Owner owner = ownerOptional.get();
            return buildUserDetails(
                    owner.getEmail(),
                    owner.getPassword(),
                    owner.getRole().name()
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    // =========================
    // Load User from JWT Token
    // =========================
    public UserDetails loadUserByJwtToken(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String jwt = token.substring(7);
        String email = jwtUtil.extractEmail(jwt);

        return loadUserByUsername(email);
    }

    // =========================
    // Get Logged User Info from Token
    // =========================
    public LoggedUser loadUserByToken(String token) {

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Authorization header is missing");
        }

        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header format");
        }

        String jwt = token.substring(7).trim();
        String email = jwtUtil.extractEmail(jwt);

        // Check Tenant
        Optional<Tenant> tenantOptional = tenantRepository.findByEmail(email);
        if (tenantOptional.isPresent()) {
            Tenant tenant = tenantOptional.get();
            return new LoggedUser(
                    tenant.getId(),
                    tenant.getFullName(),
                    tenant.getRole()
            );
        }

        // Check Admin
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return new LoggedUser(
                    admin.getId(),
                    admin.getFullName(),
                    admin.getRole()
            );
        }

        // Check Owner
        Optional<Owner> ownerOptional = ownerRepository.findByEmail(email);
        if (ownerOptional.isPresent()) {
            Owner owner = ownerOptional.get();
            return new LoggedUser(
                    owner.getId(),
                    owner.getFullName(),
                    owner.getRole()
            );
        }

        throw new UsernameNotFoundException("User not found for token");
    }

    // =========================
    // Common Method to Build Spring Security User
    // =========================
    private UserDetails buildUserDetails(String email,
                                         String password,
                                         String role) {

        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                Collections.singleton(() -> "ROLE_" + role)
        );
    }
}