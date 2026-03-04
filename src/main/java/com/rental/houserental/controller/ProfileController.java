package com.rental.houserental.controller;

import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.dto.ProfileDto;
import com.rental.houserental.entity.Profile;
import com.rental.houserental.service.CustomUserDetails;
import com.rental.houserental.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private CustomUserDetails customUserDetailsService;

    @Autowired
    private ProfileService profileService;

    private final String uploadDir = "uploads/profile";

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = customUserDetailsService.loadUserByToken(authHeader);
        Profile profile = profileService.getProfile(user.getId(), user.getRole());
        if (profile.getImages() != null && !profile.getImages().isEmpty()) {
            profile.setImages("/uploads/profile/" + profile.getImages());
        }

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @ModelAttribute ProfileDto profileDto) throws Exception {

        LoggedUser user = customUserDetailsService.loadUserByToken(authHeader);

        // Ensure upload directory exists
        File directory = new File(uploadDir);
        if (!directory.exists()) directory.mkdirs();

        // Handle image upload
        MultipartFile imageFile = profileDto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName).toAbsolutePath();
            Files.write(path, imageFile.getBytes());
            profileDto.setImages(fileName);
        }

        Profile updatedProfile = profileService.updateProfile(
                user.getId(),
                user.getRole(),
                profileDto
        );

        if (updatedProfile.getImages() != null && !updatedProfile.getImages().isEmpty()) {
            updatedProfile.setImages("/uploads/profile/" + updatedProfile.getImages());
        }

        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProfile(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = customUserDetailsService.loadUserByToken(authHeader);
        Profile profile = profileService.getProfile(user.getId(), user.getRole());

        // Delete profile image if exists
        if (profile.getImages() != null && !profile.getImages().isEmpty()) {
            File file = new File(uploadDir + "/" + profile.getImages());
            if (file.exists()) file.delete();
        }

        profileService.deleteProfile(user.getId(), user.getRole());
        return new ResponseEntity<>("Profile deleted successfully", HttpStatus.OK);
    }
}