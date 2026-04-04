package com.rental.houserental.algorithm;

import com.rental.houserental.entity.Property;
import java.util.Comparator;
import java.util.List;

public class SortProperties {


    public static List<Property> QuickSort(List<Property> properties, String order, String basisOn) {
        if (properties == null || properties.isEmpty()) return properties;

        Comparator<Property> comparator;

        // Determine primary sorting criteria
        switch (basisOn.toLowerCase()) {
            case "area":
                comparator = Comparator.comparing(Property::getArea);
                break;
            case "location":
                comparator = Comparator.comparing(Property::getDistrict);
                break;
            case "price":
                comparator=Comparator.comparing(Property:: getPrice);
                break;
            default:
                comparator = Comparator.comparing(Property::getCreatedAt)
                        .thenComparing(Property::getDistrict)
                        .thenComparing(Property::getArea);
                break;
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        properties.sort(comparator);
        return properties;
    }
}