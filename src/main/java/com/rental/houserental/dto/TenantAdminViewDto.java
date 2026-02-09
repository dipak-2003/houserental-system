package com.rental.houserental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TenantAdminViewDto {
    private Long id;
    private String fullName;
    private String email;
    private String role;
}
