package com.rental.houserental.service.impl;

import com.rental.houserental.entity.Admin;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.enums.PropertyStatus;
import com.rental.houserental.repository.AdminRepository;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.PropertyRepository;
import com.rental.houserental.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private final EmailService emailService;
    private final PropertyRepository propertyRepository;
    private final OwnerRepository ownerRepository;
    @Autowired
    private AdminRepository adminRepository;
    // OWNER OPERATIONS
    @Override
    public Property addProperty(Long ownerId, Property property) throws Exception {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        property.setOwner(owner);
        property.setStatus(PropertyStatus.PENDING);
        property.setBookingStatus(BookingStatus.AVAILABLE);

        return propertyRepository.save(property);
    }

    @Override
    public Property updateProperty(Long propertyId, Long ownerId, Property property) throws Exception {
        Owner owner=ownerRepository.findById(ownerId).orElseThrow(()->new RuntimeException("Owner not founds"));
        property.setOwner(owner);
        Property exists=propertyRepository.findById(propertyId).orElseThrow(()->new RuntimeException("Property id not found!"));
        property.setId(exists.getId());
        return propertyRepository.save(property);
    };


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
    public Property approveProperty(Long propertyId, Long adminId) throws Exception {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        Admin admin=adminRepository.findById(adminId)
                .orElseThrow(()->new RuntimeException("Unauthorized user"));
        property.setAdmin(admin);
        property.setStatus(PropertyStatus.APPROVED);
        property.setBookingStatus(BookingStatus.AVAILABLE);
        Property savedProperty = propertyRepository.save(property);

        // EMAIL NOTIFICATION
    String ownerEmail = savedProperty.getOwner().getEmail();
    String propertyTitle = savedProperty.getTitle();

    emailService.  sendPropertyApprovalEmail(ownerEmail, propertyTitle);
        return savedProperty;
    }

    @Override
    public Property rejectProperty(Long propertyId, Long adminId) throws Exception {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        Admin admin=adminRepository.findById(adminId)
                .orElseThrow(()->new RuntimeException("Unauthorized user"));
        property.setAdmin(admin);
        property.setStatus(PropertyStatus.REJECTED);

        return propertyRepository.save(property);
    }

    @Override
    public void deletePropertyByAdmin(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        propertyRepository.delete(property);
    }

    @Override
    public List<Property> getAllProperties() throws Exception {
        return propertyRepository.findAllWithOwner(PropertyStatus.APPROVED);


    }

    // TENANT / PUBLIC OPERATIONS
    @Override
    public List<Property> getAvailableProperties() {
        return propertyRepository.findByBookingStatus(BookingStatus.AVAILABLE);
    }

    @Override
    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @Override
    public Property getPropertyWithOwner(Long propertyId) {
        return propertyRepository.findByIdWithOwner(propertyId,PropertyStatus.APPROVED).orElseThrow();
    }

    @Override
    public List<Property> searchProperty(String tole, String municipality, String type, Double minPrice, Double maxPrice, Integer bedrooms) {
        BookingStatus status = BookingStatus.AVAILABLE;
        return propertyRepository.searchProperty(tole, municipality, type, minPrice, maxPrice, bedrooms, status);
    }


}