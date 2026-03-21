package com.rental.houserental.controller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.dto.OwnerDashDto;
import com.rental.houserental.dto.PropertyDto;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.service.CustomUserDetails;
import com.rental.houserental.service.PropertyService;
import com.rental.houserental.service.impl.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/owner")

public class OwnerController {

    @Autowired
    private  OwnerRepository ownerRepository;
    @Autowired
    private PropertyService propertyService;

    @Autowired
    private CustomUserDetails userDetailsService;
    @Autowired
    private OwnerService ownerService;
    @GetMapping("/dashboard")
    public ResponseEntity<?> getOwnerDash(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        if (user == null){
            return new ResponseEntity<>("Not logged",HttpStatus.BAD_REQUEST);
        }
        OwnerDashDto ownerDashDto=ownerService.getDashDetails(user.getId());
        return new ResponseEntity<>(ownerDashDto,HttpStatus.OK);
    }


    // Add property
    @PostMapping("/add-property")
    public ResponseEntity<?> addProperty(
            @RequestHeader("Authorization") String authHeader,
            @ModelAttribute PropertyDto propertyDto) {

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null)
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            Property property = new Property();
            property.setTitle(propertyDto.getTitle());
            property.setPrice(propertyDto.getPrice());
            property.setType(propertyDto.getType());
            property.setDescription(propertyDto.getDescription());
            property.setBedrooms(propertyDto.getBedrooms());
            property.setBathrooms(propertyDto.getBathrooms());
            property.setArea(propertyDto.getArea());
            property.setFurnished(propertyDto.isFurnished());
            property.setParkingAvailable(propertyDto.isParkingAvailable());
            property.setDistrict(propertyDto.getDistrict());
            property.setMunicipality(propertyDto.getMunicipality());
            property.setWardNo(propertyDto.getWardNo());
            property.setTole(propertyDto.getTole());
            property.setHouseName(propertyDto.getHouseName());
            property.setHouseNo(propertyDto.getHouseNo());
            property.setApartmentNo(propertyDto.getApartmentNo());

            // Status
            property.setStatus(propertyDto.getStatus() != null ? propertyDto.getStatus() : PropertyStatus.PENDING);

            // Images
            if (propertyDto.getImages() != null && propertyDto.getImages().length > 0) {
                int maxImages = 2;
                MultipartFile[] images = propertyDto.getImages();
                if (images.length > maxImages) {
                    return new ResponseEntity<>("Maximum 2 images allowed", HttpStatus.BAD_REQUEST);
                }

                StringBuilder imagePaths = new StringBuilder();
                String uploadDir = System.getProperty("user.dir") + "/uploads/properties/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                for (MultipartFile file : images) {
                    if (!file.isEmpty()) {
                        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
                        Path filePath = uploadPath.resolve(fileName);
                        Files.write(filePath, file.getBytes());

                        if (!imagePaths.isEmpty()) imagePaths.append(",");
                        imagePaths.append(fileName);
                    }
                }
                property.setImageUrl(imagePaths.toString());
            }

            Property savedProperty = propertyService.addProperty(user.getId(), property);
            return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




    // Update property
    @PutMapping("/update/{propertyId}")
    public ResponseEntity<?> updateProperty(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long propertyId,
            @ModelAttribute PropertyDto propertyDto) {  // Use @ModelAttribute if images are included

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            // Map DTO to property
            Property updatedProperty = new Property();
            updatedProperty.setTitle(propertyDto.getTitle());
            updatedProperty.setPrice(propertyDto.getPrice());
            updatedProperty.setType(propertyDto.getType());
            updatedProperty.setDescription(propertyDto.getDescription());
            updatedProperty.setBedrooms(propertyDto.getBedrooms());
            updatedProperty.setBathrooms(propertyDto.getBathrooms());
            updatedProperty.setArea(propertyDto.getArea());
            updatedProperty.setFurnished(propertyDto.isFurnished());
            updatedProperty.setParkingAvailable(propertyDto.isParkingAvailable());
            // Location fields
            updatedProperty.setDistrict(propertyDto.getDistrict());
            updatedProperty.setMunicipality(propertyDto.getMunicipality());
            updatedProperty.setWardNo(propertyDto.getWardNo());
            updatedProperty.setTole(propertyDto.getTole());
            updatedProperty.setHouseName(propertyDto.getHouseName());
            updatedProperty.setHouseNo(propertyDto.getHouseNo());
            updatedProperty.setApartmentNo(propertyDto.getApartmentNo());

            // Status
            updatedProperty.setStatus(propertyDto.getStatus() != null ? propertyDto.getStatus() : PropertyStatus.PENDING);

            // Handle images if uploaded
            if (propertyDto.getImages() != null && propertyDto.getImages().length > 0) {
                int maxImages = 2;
                MultipartFile[] images = propertyDto.getImages();
                if (images.length > maxImages) {
                    return new ResponseEntity<>("Maximum 2 images allowed", HttpStatus.BAD_REQUEST);
                }

                StringBuilder imagePaths = new StringBuilder();
                String uploadDir = System.getProperty("user.dir") + "/uploads/properties/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                for (MultipartFile file : images) {
                    if (!file.isEmpty()) {
                        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
                        Path filePath = uploadPath.resolve(fileName);
                        Files.write(filePath, file.getBytes());

                        if (imagePaths.length() > 0) imagePaths.append(",");
                        imagePaths.append(fileName);
                    }
                }
                updatedProperty.setImageUrl(imagePaths.toString());
            } else {
                // Keep existing image if no new image is uploaded
                updatedProperty.setImageUrl(propertyDto.getImageUrl());
            }

            Property property = propertyService.updateProperty(propertyId, user.getId(), updatedProperty);
            return new ResponseEntity<>(property, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    // Delete property
    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<?> deleteProperty(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long propertyId) {

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            propertyService.deleteProperty(propertyId, user.getId());
            return new ResponseEntity<>("Property deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    // Get all properties of owner
    @GetMapping("/my-properties")
    public ResponseEntity<?> getOwnerProperties(@RequestHeader("Authorization") String authHeader) {
        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            List<Property> properties = propertyService.getOwnerProperties(user.getId());

            List<Property> filteredProperties = properties.stream()
                    .filter(property ->
                            !property.getStatus().equals(PropertyStatus.PENDING) &&
                                    !property.getStatus().equals(PropertyStatus.REJECTED)
                    )
                    .toList();

            return new ResponseEntity<>(filteredProperties, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    }



