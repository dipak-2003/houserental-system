package com.rental.houserental.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String email;
}
