package com.rental.houserental.dto;

import com.rental.houserental.enums.PropertyStatus;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDto {

    // Basic Info
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Location is required")
    private String location;

    @Positive(message = "Price must be greater than zero")
    private double price;

    @NotBlank(message = "Type is required") // e.g., ROOM / HOUSE / APARTMENT
    private String type;

    @Size(max = 1000, message = "Description can be at most 1000 characters")
    private String description;

    // House Details
    @Min(value = 0, message = "Bedrooms cannot be negative")
    private int bedrooms;

    @Min(value = 0, message = "Bathrooms cannot be negative")
    private int bathrooms;

    @Positive(message = "Area must be positive")
    private double area; // in sq ft

    private boolean furnished;
    private boolean parkingAvailable;

    // Image URL
    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    // Availability
    private boolean available = true;

    // Status (OPTIONAL, default could be AVAILABLE)
    private PropertyStatus status;

    // Owner ID (to associate property)
    @NotNull(message = "Owner ID is required")
    private Long ownerId;
}