package com.rental.houserental.entity;

import com.rental.houserental.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String email;
    private String fullName;
    private String password;
    private Role role;
    private String token;
}
