package com.rental.houserental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ratings")
@Data
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rateBy;

    private Long userId;

    private int rate;

    @Column(length = 1000)
    private String feedback;

    private boolean rated = false;
}