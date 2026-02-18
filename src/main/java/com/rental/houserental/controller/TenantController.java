package com.rental.houserental.controller;

import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;
import com.rental.houserental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantController {

 private final PropertyRepository propertyRepository;
    @GetMapping("/properties")
    public ResponseEntity<List<Property>> viewApprovedProperties() {
        return ResponseEntity.ok(
                propertyRepository.findByStatus(PropertyStatus.APPROVED)
        );
    }

}
