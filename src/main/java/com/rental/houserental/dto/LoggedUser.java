package com.rental.houserental.dto;

import com.rental.houserental.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggedUser {
    private Long id;
    private String fullName;
    private Role role;
}
