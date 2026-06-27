package com.rental.houserental.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agreements")
@Data
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean ownerSign;

    private boolean agreed;

    private LocalDateTime acceptedAt;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    private String adminName;
    private String adminEmail;



    // FIXED: dynamic list (NO list1, list2...)
    @ElementCollection
    @CollectionTable(
            name = "agreement_terms",
            joinColumns = @JoinColumn(name = "agreement_id")
    )
    @Column(name = "term", length = 1000)
    private List<String> terms;

    @PrePersist
    public void prePersist() {
        if (acceptedAt == null) {
            acceptedAt = LocalDateTime.now();
        }
    }
}