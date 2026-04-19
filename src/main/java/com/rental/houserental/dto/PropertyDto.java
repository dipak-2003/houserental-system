package com.rental.houserental.dto;
import com.rental.houserental.enums.PropertyStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
public class PropertyDto {

    // BASIC
    private String title;
    private Double price;
    private String type;
    private String houseNo;
    private String description;

    // LOCATION
    private String district;
    private String municipality;
    private Integer wardNo;
    private String tole;

    // DETAILS
    private Integer bedrooms;
    private Integer bathrooms;
    private Double area;
    private Boolean furnished;
    private Boolean parkingAvailable;

    private List<MultipartFile> image; // multiple files

    // DOCUMENTS
    private MultipartFile citizenFront;
    private MultipartFile citizenBack;
    private MultipartFile passportPhoto;

    private String phoneNo;

    private Boolean available;
    private PropertyStatus status;
}