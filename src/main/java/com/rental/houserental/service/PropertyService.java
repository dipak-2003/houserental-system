package com.rental.houserental.service;

import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;

import java.util.List;

public interface PropertyService {

    Property addProperty(Long ownerId, Property property) throws Exception;
    Property updateProperty(Long propertyId, Long ownerId, Property property) throws Exception;
    void deleteProperty(Long propertyId, Long ownerId) throws Exception;
    List<Property> getOwnerProperties(Long ownerId) throws Exception;
    Property approveProperty(Long propertyId, Long adminId) throws Exception;
    Property rejectProperty(Long propertyId, Long adminId) throws Exception;
    void deletePropertyByAdmin(Long propertyId);
    List<Property> getAllProperties() throws Exception;

    // TENANT / PUBLIC OPERATIONS
    List<Property> getAvailableProperties();
    Property getPropertyById(Long propertyId);
    Property getPropertyWithOwner(Long propertyId) ;
    // Search property
    List<Property> searchProperty(
            String tole,
            String municipality,
            String type,
            Double minPrice,
            Double maxPrice,
            Integer bedrooms
    );



}