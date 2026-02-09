package com.rental.houserental.repository;

import com.rental.houserental.entity.Owner;
import com.rental.houserental.enums.OwnerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByEmail(String email);

    List<Owner> findByStatus(OwnerStatus status);
}
