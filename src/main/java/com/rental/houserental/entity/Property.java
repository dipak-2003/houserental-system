package com.rental.houserental.entity;

import com.rental.houserental.enums.PropertyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private String location;           // "Kathmandu"
    private double price;              // monthly rent
    private String type;               // ROOM / HOUSE / APARTMENT

    @Column(length = 1000)
    private String description;

    // House Details
    private int bedrooms;
    private int bathrooms;
    private double area;               // in sq ft
    private boolean furnished;
    private boolean parkingAvailable;

    // Image (Single Image URL)
    private String imageUrl;

    // Availability
    private boolean available = false;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // Audit Fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Automatically set timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}