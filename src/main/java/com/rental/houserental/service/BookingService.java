package com.rental.houserental.service;

import com.rental.houserental.dto.BookedDetail;
import com.rental.houserental.entity.Booking;
import com.rental.houserental.enums.BookingStatus;

import java.util.List;

public interface BookingService {
    public Booking bookProperty(Long propertyId, Long tenantId, BookedDetail bookedDetail) throws Exception;
    public List<Booking> getAllBookings() throws Exception;
    public Booking getBooking(Long bookingId) throws Exception;
    public Booking acceptBookingOrder(Long bookingId, BookingStatus bookStatus) throws Exception;
    public Booking rejectBookingOrder(Long bookingId, BookingStatus bookStatus) throws Exception;
    public List<Booking> getByOwnerId(Long ownerId) throws Exception;
}
