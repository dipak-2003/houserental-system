package com.rental.houserental.dto;

import com.rental.houserental.enums.PropertyStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDto {

    // Basic Info
    @NotBlank(message = "Title is required")
    private String title;

    @Positive(message = "Price must be greater than zero")
    private double price;

    @NotBlank(message = "Type is required") // e.g., ROOM / HOUSE / APARTMENT
    private String type;

    @Size(max = 1000, message = "Description can be at most 1000 characters")
    private String description;

    // Location Info
    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Municipality is required")
    private String municipality;

    @Min(value = 1, message = "Ward number must be at least 1")
    private int wardNo;

    @NotBlank(message = "Tole is required")
    private String tole;

    // House/Apartment Info
    private String houseName;
    private String houseNo;
    private String apartmentNo;

    // House Details
    @Min(value = 0, message = "Bedrooms cannot be negative")
    private int bedrooms;

    @Min(value = 0, message = "Bathrooms cannot be negative")
    private int bathrooms;

    @Positive(message = "Area must be positive")
    private double area; // in sq ft

    private boolean furnished;
    private boolean parkingAvailable;

    // Images
    private MultipartFile[] images;

    // Keep for storing final image URLs in DB (comma-separated)
    private String imageUrl;

    // Availability
    private boolean available = true;

    // Status (OPTIONAL)
    private PropertyStatus status;

}