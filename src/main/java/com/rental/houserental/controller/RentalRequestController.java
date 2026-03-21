package com.rental.houserental.controller;

import com.rental.houserental.entity.RentalRequest;
import com.rental.houserental.service.RentalRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rental")
@RequiredArgsConstructor

public class    RentalRequestController {

    private final RentalRequestService rentalRequestService;

    // Tenant submits a rental request
    @PostMapping("/request")
    public ResponseEntity<RentalRequest> createRequest(@RequestBody RentalRequest request) {
        RentalRequest created = rentalRequestService.createRequest(request);
        return ResponseEntity.ok(created);
    }

    // Get all requests by a tenant
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<RentalRequest>> getRequestsByTenant(@PathVariable Long tenantId) {
        List<RentalRequest> requests = rentalRequestService.getRequestsByTenant(tenantId);
        return ResponseEntity.ok(requests);
    }

    // Get all requests for a property (for owner/admin)
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<RentalRequest>> getRequestsForProperty(@PathVariable Long propertyId) {
        List<RentalRequest> requests = rentalRequestService.getRequestsForProperty(propertyId);
        return ResponseEntity.ok(requests);
    }

    // Get all pending requests (for owner/admin dashboard)
    @GetMapping("/pending")
    public ResponseEntity<List<RentalRequest>> getPendingRequests() {
        List<RentalRequest> requests = rentalRequestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }

    // Approve a rental request
    @PutMapping("/approve/{requestId}")
    public ResponseEntity<RentalRequest> approveRequest(@PathVariable Long requestId) {
        RentalRequest approved = rentalRequestService.approveRequest(requestId);
        return ResponseEntity.ok(approved);
    }

    // Reject a rental request
    @PutMapping("/reject/{requestId}")
    public ResponseEntity<RentalRequest> rejectRequest(@PathVariable Long requestId) {
        RentalRequest rejected = rentalRequestService.rejectRequest(requestId);
        return ResponseEntity.ok(rejected);
    }
}
