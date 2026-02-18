package com.rental.houserental.controller;

import com.rental.houserental.dto.PropertyRequestDto;
import com.rental.houserental.entity.Property;
import com.rental.houserental.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerPropertiesController {


    private final PropertyService propertyService;


    @PostMapping("/properties")
    public ResponseEntity<String> submitProperty(@RequestBody PropertyRequestDto dto,
                                                 Authentication authentication) {

        String ownerEmail = authentication.getName();

        propertyService.submitProperty(dto, ownerEmail);

        return ResponseEntity.ok("Property submitted for admin approval");
    }

    // View own listings
    @GetMapping("/properties")
    public ResponseEntity<List<Property>> getMyProperties(
            Authentication authentication
    ) {
        String ownerEmail = authentication.getName();

        return ResponseEntity.ok(
                propertyService.getOwnerProperties(ownerEmail)
        );
    }
}
