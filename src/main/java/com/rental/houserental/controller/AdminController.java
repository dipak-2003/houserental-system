package com.rental.houserental.controller;

import com.rental.houserental.dto.OwnerAdminViewDto;
import com.rental.houserental.dto.TenantAdminViewDto;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.service.OwnerService;
import com.rental.houserental.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final TenantService tenantService;
    private final OwnerService ownerService;

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Welcome Admin, Dashboard Access Granted ✅");
    }

    @GetMapping("/tenants")
    public ResponseEntity<List<TenantAdminViewDto>> getAllTenants() {

        return ResponseEntity.ok(tenantService.getAllTenants());
    }
    // View all owners
    @GetMapping("/owners")
    public ResponseEntity<List<OwnerAdminViewDto>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    // Approve owner
    @PatchMapping("/owner/{id}/approve")
    public ResponseEntity<String> approveOwner(@PathVariable Long id) {
        ownerService.approveOwner(id);
        return ResponseEntity.ok("Owner approved successfully");
    }

    // Reject owner
    @PatchMapping("/owner/{id}/reject")
    public ResponseEntity<String> rejectOwner(@PathVariable Long id) {
       ownerService.rejectOwner(id);
        return ResponseEntity.ok("Owner rejected successfully");
    }
    //method to view pending owner
    @GetMapping("/owners/pending")
    public ResponseEntity<List<OwnerAdminViewDto>> viewPendingOwners() {

        return ResponseEntity.ok(ownerService.getPendingOwners());
    }


}
