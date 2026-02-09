package com.rental.houserental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OwnerLoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
