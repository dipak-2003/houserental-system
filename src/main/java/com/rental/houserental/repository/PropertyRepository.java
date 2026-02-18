package com.rental.houserental.repository;

import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwnerId(Long ownerId);

    List<Property> findByStatus(PropertyStatus status);

    Optional<Property> findByIdAndStatus(Long id, PropertyStatus status);


}
