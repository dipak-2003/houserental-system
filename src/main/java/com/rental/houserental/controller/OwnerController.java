package com.rental.houserental.controller;

import com.rental.houserental.dto.OwnerProfileResponse;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerRepository ownerRepository;

    @GetMapping("/profile")
    public OwnerProfileResponse getProfile(Authentication authentication) {

        String email = authentication.getName();

        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return new OwnerProfileResponse(
                owner.getId(),
                owner.getFullName(),
                owner.getEmail(),
                owner.getPhone()
        );
    }
}
