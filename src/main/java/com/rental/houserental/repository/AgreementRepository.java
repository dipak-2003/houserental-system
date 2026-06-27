package com.rental.houserental.repository;

import com.rental.houserental.entity.Agreement;
import com.rental.houserental.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    Agreement findByOwner(Owner owner);
}