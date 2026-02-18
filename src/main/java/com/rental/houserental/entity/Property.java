package com.rental.houserental.entity;

import com.rental.houserental.enums.PropertyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String title;          // "2BHK Apartment"
    private String location;       // "Kathmandu"
    private double price;          // monthly rent
    private String type;           // ROOM / HOUSE / APARTMENT

    @Column(length = 1000)
    private String description;

    private boolean available = false; // only true after approval


    @Enumerated(EnumType.STRING)
    private PropertyStatus status;


    // Relationship: Many properties belong to one owner
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

}
