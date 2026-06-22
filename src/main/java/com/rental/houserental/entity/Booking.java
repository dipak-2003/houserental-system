package com.rental.houserental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rental.houserental.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
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

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private Owner owner;

    private String phone;

    private String address;

    // number of days
    private int rentTime;

    private LocalDate bookedAt;

    private LocalDate bookedEndedAt;

    @PrePersist
    public void onCreate() {
        if (bookedAt == null) {
            bookedAt = LocalDate.now();
        }
        calculateEndDate();
    }

    @PreUpdate
    public void onUpdate() {
        calculateEndDate();
    }

    private void calculateEndDate() {
        if (bookedAt != null && rentTime > 0) {
            bookedEndedAt = bookedAt.plusDays(rentTime);
        }
    }
}