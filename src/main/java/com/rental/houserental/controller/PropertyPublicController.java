package com.rental.houserental.controller;

import com.rental.houserental.dto.PropertyPublicDto;
import com.rental.houserental.service.PropertyPublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/properties/public")
@RequiredArgsConstructor
public class PropertyPublicController {

    private final PropertyPublicService propertyPublicService;

    //  Public list endpoint
    @GetMapping
    public ResponseEntity<List<PropertyPublicDto>> getAllApprovedProperties() {
        return ResponseEntity.ok(propertyPublicService.getApprovedProperties());
    }

    //  Public detail endpoint
    @GetMapping("/{id}")
    public ResponseEntity<PropertyPublicDto> getPropertyDetail(@PathVariable Long id) {
        return ResponseEntity.ok(propertyPublicService.getApprovedPropertyById(id));
    }

}
