package com.rental.houserental.repository;

import com.rental.houserental.entity.Booking;
import com.rental.houserental.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    Optional<Booking> findByTenantId(Long tenantId) ;
    Long countByStatus(BookingStatus status);
}
