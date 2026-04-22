package com.rental.houserental.repository;

import com.rental.houserental.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    //Find payment by transaction ID (pid)
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByOwner_Id(Long id);
    long countByOwner_Id(Long ownerId);


}