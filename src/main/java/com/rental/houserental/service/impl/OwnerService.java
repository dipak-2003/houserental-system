package com.rental.houserental.service.impl;
import com.rental.houserental.dto.OwnerDashDto;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OwnerService {
    @Autowired
     private PropertyRepository propertyRepository;
      public OwnerDashDto getDashDetails(Long ownerId){
          OwnerDashDto ownerDashDto=new OwnerDashDto();
         ownerDashDto.setTotalAvailable(propertyRepository.countByOwnerIdAndBookingStatus(ownerId, BookingStatus.AVAILABLE));
          ownerDashDto.setTotalBooking(propertyRepository.countByOwnerIdAndBookingStatus(ownerId,BookingStatus.BOOKED));
          ownerDashDto.setTotalProperty(propertyRepository.countByOwnerId(ownerId));
          return ownerDashDto;
      }

}
