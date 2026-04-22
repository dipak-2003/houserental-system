package com.rental.houserental.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentCalculator {

    // Inject value from application.properties
    private double charge=2;
    // Calculate charge percentage
    public double calculation(double amount){
        return (amount * charge) / 100;
    }
}