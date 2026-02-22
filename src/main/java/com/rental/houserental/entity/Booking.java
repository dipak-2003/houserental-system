package com.rental.houserental.entity;

import com.rental.houserental.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    // Tenant who books
    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // Property being booked
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    // Owner of property
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;



}
