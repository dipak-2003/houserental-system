package com.rental.houserental.service;

import com.rental.houserental.dto.PropertyPublicDto;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;
import com.rental.houserental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyPublicService {

    private final PropertyRepository propertyRepository;

    // ✅ List all approved properties
    public List<PropertyPublicDto> getApprovedProperties() {

        return propertyRepository.findByStatus(PropertyStatus.APPROVED)
                .stream()
                .map(p -> new PropertyPublicDto(
                        p.getId(),
                        p.getTitle(),
                        p.getLocation(),
                        p.getPrice(),
                        p.getDescription()
                ))
                .toList();
    }

    // ✅ View one approved property detail
    public PropertyPublicDto getApprovedPropertyById(Long id) {

        Property property = propertyRepository
                .findByIdAndStatus(id, PropertyStatus.APPROVED)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        return new PropertyPublicDto(
                property.getId(),
                property.getTitle(),
                property.getLocation(),
                property.getPrice(),
                property.getDescription()
        );
    }
}
