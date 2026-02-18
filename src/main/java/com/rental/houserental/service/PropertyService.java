package com.rental.houserental.service;

import com.rental.houserental.dto.PropertyRequestDto;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final OwnerRepository ownerRepository;
    public void submitProperty(PropertyRequestDto dto, String ownerEmail) {

        Owner owner = ownerRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Property property = new Property();

        property.setTitle(dto.getTitle());
        property.setLocation(dto.getLocation());
        property.setPrice(dto.getPrice());

        //  Important: Always PENDING when owner lists
        property.setStatus(PropertyStatus.PENDING);

        // link property to owner
        property.setOwner(owner);

        propertyRepository.save(property);
    }

    // Owner views own listings
    public List<Property> getOwnerProperties(String ownerEmail) {

        Owner owner = ownerRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return propertyRepository.findByOwnerId(owner.getId());
    }
    // Tenant will see ONLY approved listings
    public List<Property> viewApprovedProperties() {
        return propertyRepository.findByStatus(PropertyStatus.APPROVED);
    }

}
