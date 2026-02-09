package com.rental.houserental.service;

import com.rental.houserental.dto.AdminLoginRequest;
import com.rental.houserental.dto.AuthResponse;
import com.rental.houserental.dto.OwnerLoginRequest;
import com.rental.houserental.dto.TenantLoginRequest;
import com.rental.houserental.entity.Admin;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Tenant;
import com.rental.houserental.repository.AdminRepository;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.security.JwtUtil;
import com.rental.houserental.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TenantRepository tenantRepository;
    private final OwnerRepository ownerRepository;
    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse loginTenant(TenantLoginRequest request) {

        Tenant tenant = tenantRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), tenant.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(
                tenant.getEmail(),
                tenant.getRole()
        );

        return new AuthResponse(token);
    }
    public AuthResponse loginOwner(OwnerLoginRequest request) {

        Owner owner = ownerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(owner.getEmail(), owner.getRole());

        return new AuthResponse(token);
    }
    public AuthResponse loginAdmin(AdminLoginRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid admin credentials");
        }

        String token = jwtUtil.generateToken(admin.getEmail(), admin.getRole());

        return new AuthResponse(token);
    }



}
