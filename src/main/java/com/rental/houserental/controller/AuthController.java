package com.rental.houserental.controller;

import com.rental.houserental.dto.*;
import com.rental.houserental.entity.Tenant;
import com.rental.houserental.service.AuthService;
import com.rental.houserental.service.OwnerService;
import com.rental.houserental.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final TenantService tenantService;
    private final OwnerService ownerService;

    private final AuthService authService;


    @PostMapping("/tenant/register")
    public ResponseEntity<String> registerTenant(
            @Valid @RequestBody TenantRegisterRequest request) {

        tenantService.registerTenant(request);
        return ResponseEntity.ok("Tenant registered successfully");
    }
    @PostMapping("/tenant/login")
    public ResponseEntity<AuthResponse> loginTenant(
            @Valid @RequestBody TenantLoginRequest request) {

        return ResponseEntity.ok(authService.loginTenant(request));
    }
    @GetMapping("/profile")
    public ResponseEntity<TenantProfileDTO> profile(Authentication authentication) {

        String email = authentication.getName(); // from JWT

        Tenant tenant = tenantService.getTenantByEmail(email);

        TenantProfileDTO dto = new TenantProfileDTO(
                tenant.getFullName(),
                tenant.getEmail(),
                tenant.getRole()
        );


        return ResponseEntity.ok(dto);
    }
    @PostMapping("/owner/register")
    public ResponseEntity<String> registerOwner(
            @Valid @RequestBody OwnerRegisterRequest request) {

        ownerService.registerOwner(request);
        return ResponseEntity.ok("Owner registered successfully");
    }

    @PostMapping("/owner/login")
    public ResponseEntity<AuthResponse> loginOwner(
            @Valid @RequestBody OwnerLoginRequest request) {

        return ResponseEntity.ok(authService.loginOwner(request));
    }
    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> loginAdmin(
            @Valid @RequestBody AdminLoginRequest request) {

        return ResponseEntity.ok(authService.loginAdmin(request));
    }




}
