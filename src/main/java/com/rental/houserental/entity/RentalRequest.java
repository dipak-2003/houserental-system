package com.rental.houserental.entity;

import com.rental.houserental.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rental_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Which property is requested
        @ManyToOne
        @JoinColumn(name = "property_id")
        private Property property;

        // Which tenant requested
        @ManyToOne
        @JoinColumn(name = "tenant_id")
        private Tenant tenant;

        // Optional message from tenant
        private String message;

        // Request status (PENDING, APPROVED, REJECTED)
        @Enumerated(EnumType.STRING)
        private RequestStatus status;

        // When request was made
        private LocalDateTime requestDate;
    }


