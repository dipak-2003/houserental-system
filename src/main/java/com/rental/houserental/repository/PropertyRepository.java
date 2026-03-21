package com.rental.houserental.repository;

import com.rental.houserental.entity.Booking;
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
    Long countByBookingStatus(BookingStatus status);
    List<Property> findByOwner(Owner owner);
    List<Property> findByBookingStatus(BookingStatus bookingStatus);

    @Query("SELECT p FROM Property p " +
            "WHERE (:keyword IS NULL OR " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(p.tole) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(p.municipality) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(p.district) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(p.type) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(p.houseName) LIKE LOWER(CONCAT('%', :keyword, '%')))) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:minArea IS NULL OR p.area >= :minArea) " +
            "AND (:maxArea IS NULL OR p.area <= :maxArea) " +
            "AND (:bedrooms IS NULL OR p.bedrooms = :bedrooms) " +
            "AND (:typeFilter IS NULL OR LOWER(p.type) = LOWER(:typeFilter))")
    List<Property> searchByKeywordAndFilters(
            @Param("keyword") String keyword,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minArea") Double minArea,
            @Param("maxArea") Double maxArea,
            @Param("bedrooms") Integer bedrooms,
            @Param("typeFilter") String typeFilter
    );

    @Query("SELECT p FROM Property p JOIN FETCH p.owner WHERE p.status = :status")
    List<Property> findAllWithOwner(@Param("status") PropertyStatus status);
    @Query("SELECT p FROM Property p JOIN FETCH p.owner WHERE p.id = :id AND p.status = :status")
    Optional<Property> findByIdWithOwner(@Param("id") Long id,@Param("status") PropertyStatus status);
    Long countByOwnerIdAndBookingStatus(Long ownerId,BookingStatus bookingStatus);
    Long countByOwnerId(Long ownerId);
}