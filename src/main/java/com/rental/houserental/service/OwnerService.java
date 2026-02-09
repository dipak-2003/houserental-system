package com.rental.houserental.service;

import com.rental.houserental.dto.OwnerAdminViewDto;
import com.rental.houserental.dto.OwnerRegisterRequest;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.enums.OwnerStatus;
import com.rental.houserental.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerOwner(OwnerRegisterRequest request) {

        if (ownerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Owner owner = new Owner();
        owner.setFullName(request.getFullName());
        owner.setEmail(request.getEmail());
        owner.setPassword(passwordEncoder.encode(request.getPassword()));
        owner.setPhone(request.getPhone());
        owner.setStatus(OwnerStatus.PENDING);
        owner.setRole("ROLE_OWNER");

        ownerRepository.save(owner);
    }
//for viewing the all owners in the system
    public List<OwnerAdminViewDto> getAllOwners() {
        return ownerRepository.findAll()
                .stream()
                .map(o -> new OwnerAdminViewDto(
                        o.getId(),
                        o.getFullName(),
                        o.getEmail(),
                        o.getPhone(),
                        o.getStatus()
                ))
                .toList();
    }
//for approving the owner
    public void approveOwner(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        owner.setStatus(OwnerStatus.APPROVED);
        ownerRepository.save(owner);
    }
//for rejecting the owner
    public void rejectOwner(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        owner.setStatus(OwnerStatus.REJECTED);
        ownerRepository.save(owner);
    }

    //logic to view all the pending owners
    public List<OwnerAdminViewDto> getPendingOwners() {

        return ownerRepository.findByStatus(OwnerStatus.PENDING)
                .stream()
                .map(o -> new OwnerAdminViewDto(
                        o.getId(),
                        o.getFullName(),
                        o.getEmail(),
                        o.getPhone(),
                        o.getStatus()
                ))
                .toList();
    }

}
