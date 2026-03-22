package com.rental.houserental.controller;

import com.rental.houserental.dto.BookedDetail;
import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.entity.Booking;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.service.BookingService;
import com.rental.houserental.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequestMapping("/api")
@RestController
public class BookingController {

    @Autowired
    private CustomUserDetails userDetailsService;
    @Autowired
    private BookingService bookingService;

    @PostMapping("/createBook/{id}")
    public ResponseEntity<?> bookProperty(@PathVariable Long id,
                                          @RequestBody BookedDetail bookedDetail,
                                          @RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        Booking booking = bookingService.bookProperty(id, user.getId(),bookedDetail);
        return new ResponseEntity<Booking>(booking, HttpStatus.OK);
    }

    @GetMapping("/getAllBooking")
    public ResponseEntity<?> getAllBooking(
            @RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        List<Booking> booking = bookingService.getAllBookings();
        if (user != null) {
            return new ResponseEntity<List<Booking>>(booking, HttpStatus.OK);
        }
        return new ResponseEntity<String>("User not login", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getBooking/{id}")
    public ResponseEntity<?> getBooking(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        Booking booking = bookingService.getBooking(id);
        if (user != null) {
            return new ResponseEntity<Booking>(booking, HttpStatus.OK);
        }
        return new ResponseEntity<String>("User not login", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateBookAccept/{id}")
    public ResponseEntity<?> updateBookingBooking(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) throws Exception {

        LoggedUser user = userDetailsService.loadUserByToken(authHeader);

        if (user != null) {
            Booking booking = bookingService.acceptBookingOrder(id, BookingStatus.BOOKED);
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }

        return new ResponseEntity<>("User not login", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateBookReject/{id}")
    public ResponseEntity<?> updateBookingReject(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) throws Exception {

        LoggedUser user = userDetailsService.loadUserByToken(authHeader);

        if (user != null) {
            Booking booking = bookingService.rejectBookingOrder(id, BookingStatus.REJECTED);
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }

        return new ResponseEntity<>("User not login", HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/owner-booking")
    public ResponseEntity<?> getBookingWithOwner(
            @RequestHeader("Authorization") String authHeader) throws Exception {
        LoggedUser user = userDetailsService.loadUserByToken(authHeader);
        List<Booking> bookings = null;
        if (user != null) {
            bookings = bookingService.getByOwnerId(user.getId());
        }
        return new ResponseEntity<List<Booking>>(bookings, HttpStatus.OK);

    }
}
