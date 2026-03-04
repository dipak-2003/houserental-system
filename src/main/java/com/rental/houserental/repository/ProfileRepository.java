package com.rental.houserental.repository;

import com.rental.houserental.entity.Profile;
import com.rental.houserental.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Optional<Profile> findByAdminId(Long adminId);
    Optional<Profile> findByOwnerId(Long ownerId);
    Optional<Profile> findByTenantId(Long tenantId);
}
