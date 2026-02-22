package com.rental.houserental.controller;

import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.dto.OwnerProfileResponse;
import com.rental.houserental.dto.PropertyDto;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Property;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.service.CustomUserDetails;
import com.rental.houserental.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")

public class OwnerController {

    @Autowired
    private  OwnerRepository ownerRepository;
    @Autowired
    private PropertyService propertyService;

    @Autowired
    private CustomUserDetails userDetailsService;

    @GetMapping("/profile")
    public OwnerProfileResponse getProfile(Authentication authentication) {

        String email = authentication.getName();

        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return new OwnerProfileResponse(
                owner.getId(),
                owner.getFullName(),
                owner.getEmail(),
                owner.getPhone()
        );
    }


    // Add property
    @PostMapping("/add")
    public ResponseEntity<?> addProperty(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody PropertyDto propertyDto) {

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            Property property = new Property();
            property.setTitle(propertyDto.getTitle());
            property.setLocation(propertyDto.getLocation());
            property.setPrice(propertyDto.getPrice());
            property.setType(propertyDto.getType());
            property.setDescription(propertyDto.getDescription());
            property.setBedrooms(propertyDto.getBedrooms());
            property.setBathrooms(propertyDto.getBathrooms());
            property.setArea(propertyDto.getArea());
            property.setFurnished(propertyDto.isFurnished());
            property.setParkingAvailable(propertyDto.isParkingAvailable());
            property.setImageUrl(propertyDto.getImageUrl());
            property.setAvailable(propertyDto.isAvailable());

            Property savedProperty = propertyService.addProperty(user.getId(), property);
            return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update property
    @PutMapping("/update/{propertyId}")
    public ResponseEntity<?> updateProperty(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long propertyId,
            @Valid @RequestBody PropertyDto propertyDto) {

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            Property updatedProperty = new Property();
            updatedProperty.setTitle(propertyDto.getTitle());
            updatedProperty.setLocation(propertyDto.getLocation());
            updatedProperty.setPrice(propertyDto.getPrice());
            updatedProperty.setType(propertyDto.getType());
            updatedProperty.setDescription(propertyDto.getDescription());
            updatedProperty.setBedrooms(propertyDto.getBedrooms());
            updatedProperty.setBathrooms(propertyDto.getBathrooms());
            updatedProperty.setArea(propertyDto.getArea());
            updatedProperty.setFurnished(propertyDto.isFurnished());
            updatedProperty.setParkingAvailable(propertyDto.isParkingAvailable());
            updatedProperty.setImageUrl(propertyDto.getImageUrl());

            Property property = propertyService.updateProperty(propertyId, user.getId(), updatedProperty);
            return new ResponseEntity<>(property, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Delete property
    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<?> deleteProperty(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long propertyId) {

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            propertyService.deleteProperty(propertyId, user.getId());
            return new ResponseEntity<>("Property deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get all properties of owner
    @GetMapping("/my-properties")
    public ResponseEntity<?> getOwnerProperties(@RequestHeader("Authorization") String authHeader) {
        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            List<Property> properties = propertyService.getOwnerProperties(user.getId());
            return new ResponseEntity<>(properties, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    }



