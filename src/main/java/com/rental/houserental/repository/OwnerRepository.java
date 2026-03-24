package com.rental.houserental.repository;

import com.rental.houserental.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByEmail(String email);
    Owner findByResetToken(String token);
}
