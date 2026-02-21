package com.rental.houserental.dto;
import com.rental.houserental.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private Role role;
}
