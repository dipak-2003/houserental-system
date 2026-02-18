package com.rental.houserental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class PropertyPublicDto {
    private Long id;
    private String title;
    private String location;
    private double price;
    private String description;

}
