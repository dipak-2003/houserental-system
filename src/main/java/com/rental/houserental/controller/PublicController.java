package com.rental.houserental.controller;

import com.rental.houserental.dto.PropertyResponseDto;
import com.rental.houserental.entity.Property;
import com.rental.houserental.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/all-properties")
    public ResponseEntity<List<PropertyResponseDto>> getAllProperties() throws Exception {

        List<Property> properties = propertyService.getAllProperties();

        // Filter only paid properties
        List<Property> paidProperties = properties.stream()
                .filter(Property::isPayment_status)
                .toList();

        List<PropertyResponseDto> responseList = new ArrayList<>();

        for (Property p : paidProperties) {

            PropertyResponseDto dto = new PropertyResponseDto();

            dto.setId(p.getId());
            dto.setTitle(p.getTitle());
            dto.setPrice(p.getPrice());
            dto.setType(p.getType());
            dto.setDescription(p.getDescription());

            dto.setDistrict(p.getDistrict());
            dto.setMunicipality(p.getMunicipality());
            dto.setWardNo(p.getWardNo());
            dto.setTole(p.getTole());
            dto.setHouseNo(p.getHouseNo());

            dto.setBedrooms(p.getBedrooms());
            dto.setBathrooms(p.getBathrooms());
            dto.setArea(p.getArea());

            dto.setFurnished(p.isFurnished());
            dto.setParkingAvailable(p.isParkingAvailable());

            dto.setImageUrl(p.getImages());

            dto.setBookingStatus(p.getBookingStatus());

            // Owner details
            if (p.getOwner() != null) {
                dto.setOwnerId(p.getOwner().getId());
                dto.setOwnerName(p.getOwner().getFullName());
                dto.setOwnerEmail(p.getOwner().getEmail());
            }

            responseList.add(dto);
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/property/{id}")
    public ResponseEntity<PropertyResponseDto> getPropertyByID(
            @PathVariable Long id) {

        Property p = propertyService.getPropertyWithOwner(id);

        // Property not found
        if (p == null) {
            return ResponseEntity.notFound().build();
        }

        // Payment not completed
        if (!p.isPayment_status()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        PropertyResponseDto dto = new PropertyResponseDto();

        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setPrice(p.getPrice());
        dto.setType(p.getType());
        dto.setDescription(p.getDescription());

        dto.setDistrict(p.getDistrict());
        dto.setMunicipality(p.getMunicipality());
        dto.setWardNo(p.getWardNo());
        dto.setTole(p.getTole());
        dto.setHouseNo(p.getHouseNo());

        dto.setBedrooms(p.getBedrooms());
        dto.setBathrooms(p.getBathrooms());
        dto.setArea(p.getArea());

        dto.setFurnished(p.isFurnished());
        dto.setParkingAvailable(p.isParkingAvailable());

        dto.setImageUrl(p.getImages());

        dto.setBookingStatus(p.getBookingStatus());

        if (p.getOwner() != null) {
            dto.setOwnerId(p.getOwner().getId());
            dto.setOwnerName(p.getOwner().getFullName());
            dto.setOwnerEmail(p.getOwner().getEmail());
        }

        return ResponseEntity.ok(dto);
    }
}