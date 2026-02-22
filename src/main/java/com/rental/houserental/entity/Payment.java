package com.rental.houserental.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="payments")
@Data
public class Payment {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;


}
