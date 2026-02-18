package com.rental.houserental.service;

import com.rental.houserental.dto.PendingPropertyAdminDto;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;
import com.rental.houserental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPropertyService {

    private final PropertyRepository propertyRepository;
    public List<PendingPropertyAdminDto> getPendingProperties() {

        return propertyRepository.findByStatus(PropertyStatus.PENDING)
                .stream()
                .map(p -> new PendingPropertyAdminDto(
                        p.getId(),
                        p.getTitle(),
                        p.getLocation(),
                        p.getPrice(),
                        p.getStatus(),

                        p.getOwner().getId(),
                        p.getOwner().getFullName(),
                        p.getOwner().getEmail()
                ))
                .toList();
    }


    // Approve listing
    public void approveProperty(Long propertyId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setStatus(PropertyStatus.APPROVED);
        property.setAvailable(true);

        propertyRepository.save(property);
    }

    // Reject listing
    public void rejectProperty(Long propertyId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setStatus(PropertyStatus.REJECTED);
        property.setAvailable(false);

        propertyRepository.save(property);
    }
}
