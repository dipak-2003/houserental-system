package com.rental.houserental.dto;

import lombok.Data;

@Data
public class OwnerDashDto {

    private Long totalBooking;
    private Long  totalProperty;
    private Long totalAvailable;

}
