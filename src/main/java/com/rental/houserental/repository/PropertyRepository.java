package com.rental.houserental.repository;

import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwnerId(Long ownerId);

    List<Property> findByStatus(PropertyStatus status);

    Optional<Property> findByIdAndStatus(Long id, PropertyStatus status);
    Long countByStatus(PropertyStatus status);
    List<Property> findByOwner(Owner owner);

    List<Property> findByAvailableTrue();

    // Search with optional filters
    @Query("SELECT p FROM Property p WHERE " +
            "(:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:bedrooms IS NULL OR p.bedrooms = :bedrooms) AND " +
            "p.available = true")
    List<Property> searchProperty(
            @Param("location") String location,
            @Param("type") String type,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("bedrooms") Integer bedrooms
    );


}
