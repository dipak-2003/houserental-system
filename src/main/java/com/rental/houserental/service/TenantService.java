package com.rental.houserental.service;

import com.rental.houserental.dto.TenantAdminViewDto;
import com.rental.houserental.dto.TenantRegisterRequest;
import com.rental.houserental.entity.Tenant;
import com.rental.houserental.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerTenant(TenantRegisterRequest request) {

        if (tenantRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Tenant tenant = new Tenant();
        tenant.setFullName(request.getFullName());
        tenant.setEmail(request.getEmail());
        tenant.setPassword(passwordEncoder.encode(request.getPassword()));
        tenant.setRole("ROLE_TENANT");

        tenantRepository.save(tenant);
    }

    // required for profile DTO
    public Tenant getTenantByEmail(String email) {
        return tenantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
    }

    // ✅ Admin Feature: View All Tenants
    public List<TenantAdminViewDto> getAllTenants() {

        return tenantRepository.findAll()
                .stream()
                .map(tenant -> new TenantAdminViewDto(
                        tenant.getId(),
                        tenant.getFullName(),
                        tenant.getEmail(),
                        tenant.getRole()
                ))
                .toList();
    }


}
