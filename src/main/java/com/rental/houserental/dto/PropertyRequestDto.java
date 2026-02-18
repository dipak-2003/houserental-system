package com.rental.houserental.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyRequestDto {

    private String title;
    private String location;
    private double price;
    private String type;
    private String description;
}
