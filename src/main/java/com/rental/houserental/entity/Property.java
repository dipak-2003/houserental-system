package com.rental.houserental.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.enums.PropertyStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    private String title;              // "2BHK Apartment"
    private double price;              // monthly rent
    private String type;               // ROOM / HOUSE / APARTMENT

    @Column(length = 1000)
    private String description;

    // Location Info
    private String district;
    private String municipality;
    private int wardNo;
    private String tole;

    // House/Apartment Info
    private String houseNo;
    private String apartmentNo;

    // House Details
    private int bedrooms;
    private int bathrooms;
    private double area;               // in sq ft
    private boolean furnished;
    private boolean parkingAvailable;
    private boolean payment_status=false;

    @ElementCollection
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();        // store comma-separated URLs if multiple

    // Availability
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus=BookingStatus.PENDING;

    // Status (APPROVED, PENDING, CANCELLED)
    @Enumerated(EnumType.STRING)
    private PropertyStatus status = PropertyStatus.PENDING;

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @JsonManagedReference// avoids infinite recursion
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonBackReference // avoids infinite recursion
    private Admin admin;

    // Audit Fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Automatically set timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = PropertyStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }






}