package com.rental.houserental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {

    private long totalTenants;
    private long totalOwners;
    private long totalProperties;
    private long totalBookings;

    private long bookedProperties;
    private long availableProperties;
}