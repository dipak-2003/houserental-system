package com.rental.houserental.dto;

import com.rental.houserental.enums.OwnerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerAdminViewDto {
    private Long id;
    private String fullName;
    private String email;
    private String phobe;
    private OwnerStatus status;


}
