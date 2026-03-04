package com.rental.houserental.repository;

import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.BookingStatus;
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
    List<Property> findByBookingStatus(BookingStatus bookingStatus);

    // Search with optional filters: tole, municipality, type, price, bedrooms
    @Query("SELECT p FROM Property p WHERE " +
            "(:tole IS NULL OR LOWER(p.tole) LIKE LOWER(CONCAT('%', :tole, '%'))) AND " +
            "(:municipality IS NULL OR LOWER(p.municipality) LIKE LOWER(CONCAT('%', :municipality, '%'))) AND " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:bedrooms IS NULL OR p.bedrooms = :bedrooms) AND " +
            "(:status IS NULL OR p.bookingStatus = :status)")
    List<Property> searchProperty(
            @Param("tole") String tole,
            @Param("municipality") String municipality,
            @Param("type") String type,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("bedrooms") Integer bedrooms,
            @Param("status") BookingStatus status
    );

    @Query("SELECT p FROM Property p JOIN FETCH p.owner WHERE p.status = :status")
    List<Property> findAllWithOwner(@Param("status") PropertyStatus status);
    @Query("SELECT p FROM Property p JOIN FETCH p.owner WHERE p.id = :id AND p.status = :status")
    Optional<Property> findByIdWithOwner(@Param("id") Long id,@Param("status") PropertyStatus status);

}