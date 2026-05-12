package com.rental.houserental.controller;

import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.dto.RateDto;
import com.rental.houserental.entity.Rating;
import com.rental.houserental.repository.RatingRepository;
import com.rental.houserental.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rate")
public class RatingController {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private CustomUserDetails customUserDetails;


    @GetMapping("get-all")
    public ResponseEntity<List<Rating>> getAllRating(){
        List<Rating> ratings=ratingRepository.findAll();
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRating(@RequestHeader("Authorization") String authHeader, @RequestBody RateDto rateDto) throws Exception {
        LoggedUser user=customUserDetails.loadUserByToken(authHeader);
        Rating rate=new Rating();
        rate.setRateBy(user.getFullName());
        rate.setUserId(user.getId());
        rate.setRate(rateDto.getRating());
        rate.setFeedback(rateDto.getFeedback());
        rate.setRated(true);
        ratingRepository.save(rate);
        return new ResponseEntity<>("Thank you to rate our system based on your experience",HttpStatus.OK);
    }

    @GetMapping("/check")
    public boolean getRatingByUser_id(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        LoggedUser user = customUserDetails.loadUserByToken(authHeader);

        return ratingRepository
                .findByUserId(user.getId())
                .map(Rating::isRated)
                .orElse(false);
    }
}
