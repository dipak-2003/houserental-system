package com.rental.houserental.controller;

import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.dto.OwnerDashDto;
import com.rental.houserental.dto.OwnerDoc;
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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired private PropertyRepository propertyRepository;
    @Autowired private OwnerRepository ownerRepository;
    @Autowired private PropertyService propertyService;
    @Autowired private CustomUserDetails userDetailsService;
    @Autowired private OwnerService ownerService;


    @GetMapping("/document")
    public ResponseEntity<?> getOwnerDocument(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        if (user == null) return new ResponseEntity<>("Not logged", HttpStatus.BAD_REQUEST);
        Owner owner=ownerRepository.findById(user.getId()).get();
        OwnerDoc document=new OwnerDoc();
        document.setId(owner.getId());
        document.setPhone(owner.getPhone());
        document.setEmail(owner.getEmail());
        document.setFullName(owner.getFullName());
        document.setRole(owner.getRole());
        document.setFDocImg(owner.getCitizenFrontPath());
        document.setBDocImg(owner.getCitizenBackPath());
        document.setPassPhoto(owner.getPassportPhotoPath());
        return new ResponseEntity<>(document,HttpStatus.OK);
    }


    // ================= FILE SAVE =================
    private String saveFile(MultipartFile file, String prefix, Path uploadPath) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String originalName = file.getOriginalFilename();
        String ext = "";

        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }

        String fileName = prefix + "_" + System.currentTimeMillis() + ext;
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        return fileName;
    }

    // ================= OWNER STATUS =================
    @GetMapping("/get/status")
    public ResponseEntity<?> getOwnerStatus(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        if (user == null) return new ResponseEntity<>("Not logged", HttpStatus.BAD_REQUEST);

        Owner owner = ownerRepository.findById(user.getId()).get();
        return new ResponseEntity<>(owner.isStatus(), HttpStatus.OK);
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public ResponseEntity<?> getOwnerDash(@RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        if (user == null) return new ResponseEntity<>("Not logged", HttpStatus.BAD_REQUEST);

        OwnerDashDto dto = ownerService.getDashDetails(user.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // ================= ADD PROPERTY =================
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProperty(
            @RequestHeader("Authorization") String authHeader,
            @ModelAttribute PropertyDto propertyDto) {

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            Owner owner = ownerRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));

            Property property = new Property();

            // Basic
            property.setTitle(propertyDto.getTitle());
            property.setPrice(propertyDto.getPrice());
            property.setType(propertyDto.getType());
            property.setDescription(propertyDto.getDescription());

            // Details
            property.setBedrooms(propertyDto.getBedrooms());
            property.setBathrooms(propertyDto.getBathrooms());
            property.setArea(propertyDto.getArea());
            property.setFurnished(Boolean.TRUE.equals(propertyDto.getFurnished()));
            property.setParkingAvailable(Boolean.TRUE.equals(propertyDto.getParkingAvailable()));

            // Location
            property.setDistrict(propertyDto.getDistrict());
            property.setMunicipality(propertyDto.getMunicipality());
            property.setWardNo(propertyDto.getWardNo());
            property.setTole(propertyDto.getTole());
            property.setHouseNo(propertyDto.getHouseNo());

            property.setBookingStatus(BookingStatus.NO_APPROVED);
            property.setStatus(propertyDto.getStatus() != null ? propertyDto.getStatus() : PropertyStatus.PENDING);

            // Paths
            String baseDir = System.getProperty("user.dir") + "/uploads/";
            Path propertyPath = Paths.get(baseDir + "properties/");
            Path documentPath = Paths.get(baseDir + "documents/");

            if (!Files.exists(propertyPath)) Files.createDirectories(propertyPath);
            if (!Files.exists(documentPath)) Files.createDirectories(documentPath);

            // OWNER DOCUMENTS
            if (!owner.isStatus()) {
                owner.setCitizenFrontPath(saveFile(propertyDto.getCitizenFront(), "CIT_FRONT", documentPath));
                owner.setCitizenBackPath(saveFile(propertyDto.getCitizenBack(), "CIT_BACK", documentPath));
                owner.setPassportPhotoPath(saveFile(propertyDto.getPassportPhoto(), "LIVE_PHOTO", documentPath));
                owner.setPhone(propertyDto.getPhoneNo());
                owner.setStatus(true);
                ownerRepository.save(owner);
            }

            // MULTIPLE IMAGES
            List<String> imageUrls = new ArrayList<>();

            if (propertyDto.getImage() != null && !propertyDto.getImage().isEmpty()) {

                if (propertyDto.getImage().size() > 3) {
                    return new ResponseEntity<>("Max 3 images allowed", HttpStatus.BAD_REQUEST);
                }

                for (MultipartFile file : propertyDto.getImage()) {
                    if (file != null && !file.isEmpty()) {

                        if (file.getSize() > (5 * 1024 * 1024)) {
                            return new ResponseEntity<>("Each image must be < 5MB", HttpStatus.BAD_REQUEST);
                        }

                        String fileName = saveFile(file, "PROP", propertyPath);
                        imageUrls.add(fileName);
                    }
                }
            }

            property.setImages(imageUrls);

            Property saved = propertyService.addProperty(user.getId(), property);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ================= UPDATE PROPERTY =================
    @PutMapping(value = "/update/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProperty(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long propertyId,
            @ModelAttribute PropertyDto propertyDto) {

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            if (!property.getOwner().getId().equals(user.getId())) {
                return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
            }

            // Update fields
            if (propertyDto.getTitle() != null) property.setTitle(propertyDto.getTitle());
            if (propertyDto.getPrice() != null) property.setPrice(propertyDto.getPrice());
            if (propertyDto.getType() != null) property.setType(propertyDto.getType());
            if (propertyDto.getDescription() != null) property.setDescription(propertyDto.getDescription());
            if (propertyDto.getBedrooms() != null) property.setBedrooms(propertyDto.getBedrooms());
            if (propertyDto.getBathrooms() != null) property.setBathrooms(propertyDto.getBathrooms());
            if (propertyDto.getArea() != null) property.setArea(propertyDto.getArea());
            if (propertyDto.getFurnished() != null) property.setFurnished(propertyDto.getFurnished());
            if (propertyDto.getParkingAvailable() != null) property.setParkingAvailable(propertyDto.getParkingAvailable());

            if (propertyDto.getDistrict() != null) property.setDistrict(propertyDto.getDistrict());
            if (propertyDto.getMunicipality() != null) property.setMunicipality(propertyDto.getMunicipality());
            if (propertyDto.getWardNo() != null) property.setWardNo(propertyDto.getWardNo());
            if (propertyDto.getTole() != null) property.setTole(propertyDto.getTole());
            if (propertyDto.getHouseNo() != null) property.setHouseNo(propertyDto.getHouseNo());

            if (propertyDto.getStatus() != null) property.setStatus(propertyDto.getStatus());

            // IMAGE UPDATE
            String uploadDir = System.getProperty("user.dir") + "/uploads/properties/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            if (propertyDto.getImage() != null && !propertyDto.getImage().isEmpty()) {

                if (propertyDto.getImage().size() > 3) {
                    return new ResponseEntity<>("Max 3 images allowed", HttpStatus.BAD_REQUEST);
                }

                // delete old images
                if (property.getImages() != null) {
                    for (String img : property.getImages()) {
                        Files.deleteIfExists(uploadPath.resolve(img));
                    }
                }

                List<String> newImages = new ArrayList<>();

                for (MultipartFile file : propertyDto.getImage()) {
                    if (file.getSize() > (5 * 1024 * 1024)) {
                        return new ResponseEntity<>("Each image must be < 5MB", HttpStatus.BAD_REQUEST);
                    }

                    String fileName = saveFile(file, "PROP", uploadPath);
                    newImages.add(fileName);
                }

                property.setImages(newImages);
            }

            Property updated = propertyRepository.save(property);
            return new ResponseEntity<>(updated, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<?> deleteProperty(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long propertyId) {

        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            propertyService.deleteProperty(propertyId, user.getId());
            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ================= GET OWNER PROPERTIES =================
    @GetMapping("/my-properties")
    public ResponseEntity<?> getOwnerProperties(@RequestHeader("Authorization") String authHeader) {
        try {
            LoggedUser user = userDetailsService.loadUserByToken(authHeader);
            if (user == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

            List<Property> list = propertyService.getOwnerProperties(user.getId());

            List<Property> filtered = list.stream()
                    .filter(p -> !p.getStatus().equals(PropertyStatus.PENDING)
                            && !p.getStatus().equals(PropertyStatus.REJECTED))
                    .toList();

            return new ResponseEntity<>(filtered, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // ================= GET BY ID =================
    @GetMapping("/property/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}