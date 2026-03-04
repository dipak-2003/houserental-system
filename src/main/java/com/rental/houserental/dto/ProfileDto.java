package com.rental.houserental.dto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileDto {

    private MultipartFile image;  // for upload
    private String images;        // store filename
    private String email;
    private String fullName;
    private String phone;
    private String address;
}