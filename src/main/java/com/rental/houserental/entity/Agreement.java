package com.rental.houserental.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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

    @Column(length= 1000)
    private String list1;
    @Column(length= 500)
    private String list2;
    @Column(length= 500)
    private String list3;
    @Column(length= 500)
    private String list4;
    @Column(length= 500)
    private String list5;
    @Column(length= 500)
    private String list6;
    @Column(length= 500)
    private String list7;
    @Column(length= 500)
    private String list8;
    @Column(length= 500)
    private String list9;
    @Column(length= 500)
    private String list10;

    private boolean isOwnerSign=false;
    private boolean isAdminSign=false;

    private LocalDateTime acceptedAt;
}