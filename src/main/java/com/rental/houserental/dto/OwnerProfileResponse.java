package com.rental.houserental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerProfileResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone ;
}
