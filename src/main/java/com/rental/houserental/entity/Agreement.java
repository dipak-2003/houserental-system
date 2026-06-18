package com.rental.houserental.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "agreement")
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;
    private String ownerName;
    private String adminName;

    @ElementCollection
    @CollectionTable(
            name = "agreement_terms",
            joinColumns = @JoinColumn(name = "agreement_id")
    )
    @Column(name = "term")
    private List<String> termsAndConditions = new ArrayList<>();

    private LocalDateTime acceptedAt;
}