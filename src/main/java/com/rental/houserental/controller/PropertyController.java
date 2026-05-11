package com.rental.houserental.controller;

import com.rental.houserental.algorithm.SortProperties;
import com.rental.houserental.entity.Property;
import com.rental.houserental.enums.BookingStatus;
import com.rental.houserental.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(

            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "minArea", required = false) Double minArea,
            @RequestParam(value = "maxArea", required = false) Double maxArea,
            @RequestParam(value = "bedrooms", required = false) Integer bedrooms,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "basedOn", required = false) String basedOn
    ) {

        // Fetch properties from service
        List<Property> properties = propertyService.searchProperty(
                keyword,
                type,
                minPrice,
                maxPrice,
                minArea,
                maxArea,
                bedrooms
        );

        // Print filter details
        printFunction(
                keyword,
                type,
                minPrice,
                maxPrice,
                minArea,
                maxArea,
                bedrooms,
                order,
                basedOn
        );

        // Filter only successful payment properties
        List<Property> paidProperties = properties.stream()
                .filter(Property::isPayment_status)
                .collect(Collectors.toList());

        // Sort filtered properties
        List<Property> propertyList =
                SortProperties.QuickSort(
                        paidProperties,
                        order,
                        basedOn
                );

        // Return response
        return ResponseEntity.ok(propertyList);
    }
    void printFunction(String key, String type, Double minprice, Double maxprice,
                       Double minA, Double maxA, Integer bed,
                       String order, String basedOn) {

        System.out.println("===== FILTER / SORT PARAMETERS =====");

        System.out.println("Search Key     : " + (key != null ? key : "N/A"));
        System.out.println("Type           : " + (type != null ? type : "N/A"));

        System.out.println("Min Price      : " + (minprice != null ? minprice : "N/A"));
        System.out.println("Max Price      : " + (maxprice !=null ? maxprice : "N/A"));

        System.out.println("Min Area       : " + (minA != null ? minA : "N/A"));
        System.out.println("Max Area       : " + (maxA != null ? maxA : "N/A"));

        System.out.println("Bedrooms       : " + (bed != null ? bed : "N/A"));

        System.out.println("Sort Order     : " + (order != null ? order : "N/A"));
        System.out.println("Sort Based On  : " + (basedOn != null ? basedOn : "N/A"));

        System.out.println("====================================");
    }
}