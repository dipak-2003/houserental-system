package com.rental.houserental.dto;

import com.rental.houserental.enums.BookingStatus;
import lombok.Data;

import java.util.List;

@Data
public class PropertyResponseDto {

    private Long id;
    private String title;
    private Double price;
    private String type;
    private String description;

    private String district;
    private String municipality;
    private Integer wardNo;
    private String tole;
    private String houseNo;

    private Integer bedrooms;
    private Integer bathrooms;
    private Double area;

    private Boolean furnished;
    private Boolean parkingAvailable;
    private List<String> imageUrl;
    private BookingStatus bookingStatus;
    private Long ownerId;
    private String ownerName;
    private String ownerPhone;
    private String ownerEmail;
}