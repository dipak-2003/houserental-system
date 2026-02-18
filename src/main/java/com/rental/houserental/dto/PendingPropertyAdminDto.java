package com.rental.houserental.dto;

import com.rental.houserental.enums.PropertyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PendingPropertyAdminDto {
    private Long propertyId;
    private String title;
    private String location;
    private double price;
    private PropertyStatus status;

    // Only safe owner info
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
}
