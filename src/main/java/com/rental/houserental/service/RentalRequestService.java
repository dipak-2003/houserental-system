package com.rental.houserental.service;

import com.rental.houserental.entity.Property;
import com.rental.houserental.entity.RentalRequest;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.enums.RequestStatus;
import com.rental.houserental.repository.PropertyRepository;
import com.rental.houserental.repository.RentalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalRequestService {
    private final PropertyRepository propertyRepository;
    private final RentalRequestRepository rentalRequestRepository;

    // Create a new rental request
    public RentalRequest createRequest(RentalRequest request) {
        // Check if the property already has a pending or approved request
        boolean exists = rentalRequestRepository
                .findByPropertyId(request.getProperty().getId())
                .stream()
                .anyMatch(r -> r.getStatus() == RequestStatus.PENDING || r.getStatus() == RequestStatus.APPROVED);

        if (exists) {
            throw new RuntimeException("Property already has a pending or approved request");
        }

        request.setStatus(RequestStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());
        return rentalRequestRepository.save(request);
    }

    // Get all requests for a property
    public List<RentalRequest> getRequestsForProperty(Long propertyId) {
        return rentalRequestRepository.findByPropertyId(propertyId);
    }

    // Get all requests by tenant
    public List<RentalRequest> getRequestsByTenant(Long tenantId) {
        return rentalRequestRepository.findByTenantId(tenantId);
    }


    // Approve a request
    public RentalRequest approveRequest(Long requestId) {
        RentalRequest request = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // 1Approve this request
        request.setStatus(RequestStatus.APPROVED);
        RentalRequest approvedRequest = rentalRequestRepository.save(request);

        //  Mark the property as RENTED
        Property property = request.getProperty();
        property.setBookingStatus(BookingStatus.BOOKED);
        propertyRepository.save(property);

        //  Auto-reject other pending requests for this property
        List<RentalRequest> otherRequests = rentalRequestRepository.findByPropertyId(property.getId());
        for (RentalRequest r : otherRequests) {
            if (!r.getId().equals(requestId) && r.getStatus() == RequestStatus.PENDING) {
                r.setStatus(RequestStatus.REJECTED);
                rentalRequestRepository.save(r);
            }
        }

        return approvedRequest;
    }

    // Reject a request
    public RentalRequest rejectRequest(Long requestId) {
        Optional<RentalRequest> optional = rentalRequestRepository.findById(requestId);
        if(optional.isPresent()) {
            RentalRequest request = optional.get();
            request.setStatus(RequestStatus.REJECTED);
            return rentalRequestRepository.save(request);
        }
        throw new RuntimeException("Request not found");
    }

    // Get all pending requests
    public List<RentalRequest> getPendingRequests() {
        return rentalRequestRepository.findByStatus(RequestStatus.PENDING);
    }
}
