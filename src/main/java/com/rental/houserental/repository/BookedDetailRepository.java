package com.rental.houserental.repository;

import com.rental.houserental.entity.BookedDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedDetailRepository extends JpaRepository< BookedDetail,Long> {
}
