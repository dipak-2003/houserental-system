package com.rental.houserental.repository;

import com.rental.houserental.entity.RentalRequest;
import com.rental.houserental.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRequestRepository extends JpaRepository<RentalRequest,Long>{
    // All requests of a tenant
    List<RentalRequest> findByTenantId(Long tenantId);

    // All requests for a property
    List<RentalRequest> findByPropertyId(Long propertyId);

    // All pending requests (for admin/owner)
    List<RentalRequest> findByStatus(RequestStatus status);




}
