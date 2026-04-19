package com.rental.houserental.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // eSewa transaction id (pid)
    @Column(unique = true)
    private String transactionId;

    // Payment amount
    private Double amount;

    //PENDING / SUCCESS / FAILED
    private String status;

    //eSewa reference id (rid)
    private String referenceId;

    private Long propertyId;
    private String propertyName;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
    private LocalDateTime createdAt = LocalDateTime.now();
}