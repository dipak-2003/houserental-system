package com.rental.houserental.dto;

import com.rental.houserental.enums.Role;
import lombok.Data;

@Data
public class OwnerDoc {
    private Long id;
    private String fullName;
    private String email;
    private String fDocImg;
    private String bDocImg;
    private String phone;
    private Role role;
    private String passPhoto;
}
