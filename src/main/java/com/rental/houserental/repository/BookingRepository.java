package com.rental.houserental.repository;

import com.rental.houserental.entity.Booking;
import com.rental.houserental.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.bookedDetail " +
            "LEFT JOIN FETCH b.tenant " +
            "LEFT JOIN FETCH b.property " +
            "LEFT JOIN FETCH b.owner")
    List<Booking> findAllWithDetails();

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.bookedDetail " +
            "LEFT JOIN FETCH b.tenant " +
            "LEFT JOIN FETCH b.property " +
            "LEFT JOIN FETCH b.owner " +
            "WHERE b.tenant.id = :tenantId")
    Optional<Booking> findByTenantIdWithDetails(@Param("tenantId") Long tenantId);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.bookedDetail " +
            "LEFT JOIN FETCH b.tenant " +
            "LEFT JOIN FETCH b.property " +
            "WHERE b.owner.id = :ownerId")
    List<Booking> findByOwnerIdWithDetails(@Param("ownerId") Long ownerId);

    Long countByStatus(BookingStatus status);
}