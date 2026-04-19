package com.rental.houserental.mapper;

import com.rental.houserental.dto.PropertyReviewDto;
import com.rental.houserental.entity.Property;

public class ReviewMapper {
    public static PropertyReviewDto mapToDto(Property property) {

        return PropertyReviewDto.builder()
                .id(property.getId())
                .title(property.getTitle())
                .price(property.getPrice())
                .type(property.getType())
                .description(property.getDescription())

                // Location
                .district(property.getDistrict())
                .municipality(property.getMunicipality())
                .wardNo(property.getWardNo())
                .tole(property.getTole())

                // House details
                .houseNo(property.getHouseNo())
                .bedrooms(property.getBedrooms())
                .bathrooms(property.getBathrooms())
                .area(property.getArea())

                .furnished(property.isFurnished())
                .parkingAvailable(property.isParkingAvailable())

                .imageUrl(property.getImages())
                .bookingStatus(property.getBookingStatus().name())
                .status(property.getStatus().name())

                // Owner Info
                .ownerId(property.getOwner().getId())
                .ownerName(property.getOwner().getFullName())
                .ownerEmail(property.getOwner().getEmail())
                .ownerPhone(property.getOwner().getPhone())

                // Citizenship (Admin only)
                .citizenFrontPath(property.getOwner().getCitizenFrontPath())
                .citizenBackPath(property.getOwner().getCitizenBackPath())
                .passportPhotoPath(property.getOwner().getPassportPhotoPath())

                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())

                .build();
    }
}
