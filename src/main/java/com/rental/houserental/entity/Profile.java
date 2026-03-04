package com.rental.houserental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Img_path")
    private String images;
    private String address;
    private String phone;
    private String email;
    private String fullName;




    @OneToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    @OneToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Owner owner;

    @OneToOne
    @JoinColumn(name = "tenant_id")
    @JsonIgnore
    private Tenant tenant;
}
