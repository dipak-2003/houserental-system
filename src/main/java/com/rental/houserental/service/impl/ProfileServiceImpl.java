package com.rental.houserental.service.impl;
import com.rental.houserental.dto.ProfileDto;
import com.rental.houserental.entity.Admin;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Profile;
import com.rental.houserental.entity.Tenant;
import com.rental.houserental.enums.Role;
import com.rental.houserental.repository.AdminRepository;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.ProfileRepository;
import com.rental.houserental.repository.TenantRepository;
import com.rental.houserental.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public Profile getProfile(Long userId, Role role) throws Exception {
        switch (role) {
            case ADMIN:
                return profileRepository.findByAdminId(userId)
                        .orElseGet(() -> createProfileForAdmin(userId));
            case OWNER:
                return profileRepository.findByOwnerId(userId)
                        .orElseGet(() -> createProfileForOwner(userId));
            case TENANT:
                return profileRepository.findByTenantId(userId)
                        .orElseGet(() -> createProfileForTenant(userId));
            default:
                throw new RuntimeException("Invalid role");
        }
    }

    private Profile createProfileForAdmin(Long userId) {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        Profile profile = new Profile();
        profile.setFullName(admin.getFullName());
        profile.setEmail(admin.getEmail());
        profile.setAdmin(admin);
        return profileRepository.save(profile);
    }

    private Profile createProfileForOwner(Long userId) {
        Owner owner = ownerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        Profile profile = new Profile();
        profile.setOwner(owner);
        profile.setEmail(owner.getEmail());
        profile.setFullName(owner.getFullName());
        return profileRepository.save(profile);
    }

    private Profile createProfileForTenant(Long userId) {
        Tenant tenant = tenantRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        Profile profile = new Profile();
        profile.setTenant(tenant);
        profile.setEmail(tenant.getEmail());
        profile.setFullName(tenant.getFullName());
        return profileRepository.save(profile);
    }

    @Override
    public Profile updateProfile(Long userId, Role role, ProfileDto profileDto) throws Exception {
        Profile profile = getProfile(userId, role);

        if (profileDto.getPhone() != null) profile.setPhone(profileDto.getPhone());
        if (profileDto.getAddress() != null) profile.setAddress(profileDto.getAddress());

        // Only update image if provided
        if (profileDto.getImages() != null && !profileDto.getImages().isEmpty()) {
            profile.setImages(profileDto.getImages());
        }

        return profileRepository.save(profile);
    }

    @Override
    public void deleteProfile(Long userId, Role role) throws Exception {
        Profile profile = getProfile(userId, role);
        profileRepository.delete(profile);
    }
}