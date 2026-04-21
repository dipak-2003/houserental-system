package com.rental.houserental.entity;

import com.rental.houserental.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String message;

    private Long userId;


    @Enumerated(EnumType.STRING)
    private Role role; // TENANT / OWNER / ADMIN

    private boolean isRead = false;
    private String title;
    private LocalDateTime createdAt = LocalDateTime.now();
}