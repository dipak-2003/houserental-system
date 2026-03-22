package com.rental.houserental.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class BookedDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tenant details
    private String tenantPhone;
    private String tenantAddress;

    @OneToOne
    @JoinColumn(name = "booking_id")
//    @JsonBackReference
    @JsonIgnore
    private Booking booking;
}
