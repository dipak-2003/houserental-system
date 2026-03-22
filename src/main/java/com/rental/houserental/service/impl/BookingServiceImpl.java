package com.rental.houserental.service.impl;

import com.rental.houserental.dto.BookedDetail;
import com.rental.houserental.entity.*;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.repository.BookingRepository;
import com.rental.houserental.repository.PropertyRepository;
import com.rental.houserental.repository.TenantRepository;
import com.rental.houserental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public Booking bookProperty(Long propertyId, Long tenantId, BookedDetail bookedDetail) throws Exception {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new Exception("Property not found"));
        Owner owner = property.getOwner();
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new Exception("Tenant not found"));

        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setTenant(tenant);
        booking.setOwner(owner);
        booking.setStatus(BookingStatus.PENDING);
        property.setBookingStatus(BookingStatus.REQUESTING);
        propertyRepository.save(property);
        booking.setPhone(bookedDetail.getPhone());
        booking.setAddress(bookedDetail.getAddress());

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() throws Exception {
        return bookingRepository.findAllWithDetails();
    }

    @Override
    public Booking getBooking(Long bookingId) throws Exception {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));
    }

    @Override
    public Booking acceptBookingOrder(Long bookingId, BookingStatus bookStatus) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));
        Property property = booking.getProperty();
        property.setBookingStatus(bookStatus);
        propertyRepository.save(property);

        booking.setStatus(BookingStatus.BOOKED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking rejectBookingOrder(Long bookingId, BookingStatus bookStatus) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));
        Property property = booking.getProperty();
        property.setBookingStatus(BookingStatus.AVAILABLE);
        propertyRepository.save(property);

        booking.setStatus(bookStatus);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getByOwnerId(Long ownerId) throws Exception {
        return bookingRepository.findByOwnerIdWithDetails(ownerId);
    }
}