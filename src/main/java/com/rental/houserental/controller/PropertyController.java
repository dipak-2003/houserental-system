package com.rental.houserental.controller;

import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "minArea", required = false) Double minArea,
            @RequestParam(value = "maxArea", required = false) Double maxArea,
            @RequestParam(value = "bedrooms", required = false) Integer bedrooms
    ) {
        // Call the service method with the updated parameters
        List<Property> properties = propertyService.searchProperty(
                keyword,
                type,
                minPrice,
                maxPrice,
                minArea,
                maxArea,
                bedrooms
        );

        return ResponseEntity.ok(properties);
    }
}