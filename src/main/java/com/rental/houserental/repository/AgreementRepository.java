package com.rental.houserental.repository;

import com.rental.houserental.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement,Long> {

}
