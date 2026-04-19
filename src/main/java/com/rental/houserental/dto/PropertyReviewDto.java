package com.rental.houserental.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyReviewDto {

    private Long id;
    private String title;
    private Double price;
    private String type;
    private String description;

    // Location
    private String district;
    private String municipality;
    private Integer wardNo;
    private String tole;

    // House details
    private String houseNo;
    private Integer bedrooms;
    private Integer bathrooms;
    private Double area;

    private Boolean furnished;
    private Boolean parkingAvailable;

    private List<String> imageUrl;

    private String bookingStatus;
    private String status;

    //Owner Info (Flattened)
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;

    // Citizenship Info (Admin use)
    private String citizenFrontPath;
    private String citizenBackPath;
    private String passportPhotoPath;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}