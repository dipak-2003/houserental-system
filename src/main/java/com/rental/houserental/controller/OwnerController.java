package com.rental.houserental.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.dto.OwnerDashDto;
import com.rental.houserental.dto.PropertyDto;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.enums.PropertyStatus;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.PropertyRepository;
import com.rental.houserental.service.CustomUserDetails;
import com.rental.houserental.service.PropertyService;
import com.rental.houserental.service.impl.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/owner")

public class OwnerController {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private  OwnerRepository ownerRepository;
    @Autowired
    private PropertyService propertyService;

    @Autowired
    private CustomUserDetails userDetailsService;
    @Autowired
    private OwnerService ownerService;
    private String saveFile(MultipartFile file, String prefix, Path uploadPath) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String originalName = file.getOriginalFilename();

        // Extract extension safely
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }

        String fileName = prefix + "_" + System.currentTimeMillis() + ext;

        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        return fileName;
    }

    @GetMapping("/get/status")
    public ResponseEntity<?> getOwnerStatus(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        if (user == null) {
            return new ResponseEntity<>("Not logged", HttpStatus.BAD_REQUEST);
        }
        Owner owner=ownerRepository.findById(user.getId()).get();
        return new ResponseEntity<> (owner.isStatus(),HttpStatus.OK);
    }


    @GetMapping("/dashboard")
    public ResponseEntity<?> getOwnerDash(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        if (user == null){
            return new ResponseEntity<>("Not logged",HttpStatus.BAD_REQUEST);
        }
        OwnerDashDto ownerDashDto=ownerService.getDashDetails(user.getId());
        return new ResponseEntity<>(ownerDashDto,HttpStatus.OK);
    }


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProperty(
            @RequestHeader("Authorization") String authHeader,
            @ModelAttribute PropertyDto propertyDto) {

        try {
            // 1. Authenticate
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            Owner owner = ownerRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));

            // 2. Create Property
            Property property = new Property();
            property.setTitle(propertyDto.getTitle());
            property.setPrice(propertyDto.getPrice());
            property.setType(propertyDto.getType());
            property.setDescription(propertyDto.getDescription());
            property.setBedrooms(propertyDto.getBedrooms());
            property.setBathrooms(propertyDto.getBathrooms());
            property.setArea(propertyDto.getArea());
            property.setFurnished(Boolean.TRUE.equals(propertyDto.getFurnished()));
            property.setParkingAvailable(Boolean.TRUE.equals(propertyDto.getParkingAvailable()));

            property.setDistrict(propertyDto.getDistrict());
            property.setMunicipality(propertyDto.getMunicipality());
            property.setWardNo(propertyDto.getWardNo());
            property.setTole(propertyDto.getTole());
            property.setHouseNo(propertyDto.getHouseNo());

            property.setBookingStatus(BookingStatus.AVAILABLE);
            property.setStatus(
                    propertyDto.getStatus() != null
                            ? propertyDto.getStatus()
                            : PropertyStatus.PENDING
            );

            // 3. Create Directories
            String baseDir = System.getProperty("user.dir") + "/uploads/";
            Path propertyPath = Paths.get(baseDir + "properties/");
            Path documentPath = Paths.get(baseDir + "documents/");

            if (!Files.exists(propertyPath)) Files.createDirectories(propertyPath);
            if (!Files.exists(documentPath)) Files.createDirectories(documentPath);

            // 4. Save Owner Documents
            if(!owner.isStatus()) {
                owner.setCitizenFrontPath(saveFile(propertyDto.getCitizenFront(), "CIT_FRONT", documentPath));
                owner.setCitizenBackPath(saveFile(propertyDto.getCitizenBack(), "CIT_BACK", documentPath));
                owner.setPassportPhotoPath(saveFile(propertyDto.getPassportPhoto(), "LIVE_PHOTO", documentPath));
                owner.setStatus(true);
                owner.setPhone(propertyDto.getPhoneNo());
                ownerRepository.save(owner);
            }


            // Save SINGLE Property Image
            if (propertyDto.getImage() != null && !propertyDto.getImage().isEmpty()) {
                String fileName = saveFile(propertyDto.getImage(), "PROP", propertyPath);
                property.setImageUrl(fileName);
            }

            // Save Property
            Property savedProperty = propertyService.addProperty(user.getId(), property);

            return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }





    @PutMapping(value = "/update/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProperty(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long propertyId,
            @ModelAttribute PropertyDto propertyDto) {

        try {
            // 1. Authenticate
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

            // 2. Fetch property
            Property existingProperty = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            // 3. Authorization
            if (!existingProperty.getOwner().getId().equals(user.getId())) {
                return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
            }

            // 4. Update fields (only non-null)
            if (propertyDto.getTitle() != null)
                existingProperty.setTitle(propertyDto.getTitle());

            if (propertyDto.getPrice() != null)
                existingProperty.setPrice(propertyDto.getPrice());

            if (propertyDto.getType() != null)
                existingProperty.setType(propertyDto.getType());

            if (propertyDto.getDescription() != null)
                existingProperty.setDescription(propertyDto.getDescription());

            if (propertyDto.getBedrooms() != null)
                existingProperty.setBedrooms(propertyDto.getBedrooms());

            if (propertyDto.getBathrooms() != null)
                existingProperty.setBathrooms(propertyDto.getBathrooms());

            if (propertyDto.getArea() != null)
                existingProperty.setArea(propertyDto.getArea());

            if (propertyDto.getFurnished() != null)
                existingProperty.setFurnished(propertyDto.getFurnished());

            if (propertyDto.getParkingAvailable() != null)
                existingProperty.setParkingAvailable(propertyDto.getParkingAvailable());

            // Location
            if (propertyDto.getDistrict() != null)
                existingProperty.setDistrict(propertyDto.getDistrict());

            if (propertyDto.getMunicipality() != null)
                existingProperty.setMunicipality(propertyDto.getMunicipality());

            if (propertyDto.getWardNo() != null)
                existingProperty.setWardNo(propertyDto.getWardNo());

            if (propertyDto.getTole() != null)
                existingProperty.setTole(propertyDto.getTole());

            if (propertyDto.getHouseNo() != null)
                existingProperty.setHouseNo(propertyDto.getHouseNo());

            // Status
            if (propertyDto.getStatus() != null)
                existingProperty.setStatus(propertyDto.getStatus());

            // 5. SINGLE IMAGE UPDATE
            String uploadDir = System.getProperty("user.dir") + "/uploads/properties/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 👉 If new image is uploaded → replace old one
            if (propertyDto.getImage() != null && !propertyDto.getImage().isEmpty()) {

                // (Optional) delete old image
                if (existingProperty.getImageUrl() != null) {
                    Path oldPath = uploadPath.resolve(existingProperty.getImageUrl());
                    Files.deleteIfExists(oldPath);
                }

                // save new image
                String fileName = saveFile(propertyDto.getImage(), "PROP", uploadPath);
                existingProperty.setImageUrl(fileName);
            }

            // 6. Save
            Property saved = propertyRepository.save(existingProperty);

            return new ResponseEntity<>(saved, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
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



