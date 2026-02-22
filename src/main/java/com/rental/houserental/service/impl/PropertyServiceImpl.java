package com.rental.houserental.service.impl;

import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.PropertyRepository;
import com.rental.houserental.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final OwnerRepository ownerRepository;
    // OWNER OPERATIONS
    @Override
    public Property addProperty(Long ownerId, Property property) throws Exception {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        property.setOwner(owner);
        property.setStatus(PropertyStatus.PENDING);
        property.setAvailable(false);

        return propertyRepository.save(property);
    }

    @Override
    public Property updateProperty(Long propertyId, Long ownerId, Property updatedProperty) throws Exception {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized: You can only update your own property");
        }

        property.setTitle(updatedProperty.getTitle());
        property.setLocation(updatedProperty.getLocation());
        property.setPrice(updatedProperty.getPrice());
        property.setType(updatedProperty.getType());
        property.setDescription(updatedProperty.getDescription());
        property.setBedrooms(updatedProperty.getBedrooms());
        property.setBathrooms(updatedProperty.getBathrooms());
        property.setArea(updatedProperty.getArea());
        property.setFurnished(updatedProperty.isFurnished());
        property.setParkingAvailable(updatedProperty.isParkingAvailable());
        property.setImageUrl(updatedProperty.getImageUrl());

        property.setStatus(PropertyStatus.PENDING); // Re-approval required
        property.setAvailable(false);

        return propertyRepository.save(property);
    }

    @Override
    public void deleteProperty(Long propertyId, Long ownerId) throws Exception {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own property");
        }

        propertyRepository.delete(property);
    }

    @Override
    public List<Property> getOwnerProperties(Long ownerId) throws Exception {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return propertyRepository.findByOwner(owner);
    }


    // ADMIN OPERATIONS

    @Override
    public Property approveProperty(Long propertyId, String adminId) throws Exception {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setStatus(PropertyStatus.APPROVED);
        property.setAvailable(true);

        return propertyRepository.save(property);
    }

    @Override
    public Property rejectProperty(Long propertyId, String adminId) throws Exception {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setStatus(PropertyStatus.REJECTED);
        property.setAvailable(false);

        return propertyRepository.save(property);
    }

    @Override
    public void deletePropertyByAdmin(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        propertyRepository.delete(property);
    }

    // TENANT / PUBLIC OPERATIONS
    @Override
    public List<Property> getAvailableProperties() {
        return propertyRepository.findByAvailableTrue();
    }

    @Override
    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @Override
    public List<Property> searchProperty(String location, String type, Double minPrice, Double maxPrice, Integer bedrooms) {
        return propertyRepository.searchProperty(location, type, minPrice, maxPrice, bedrooms);
    }
}