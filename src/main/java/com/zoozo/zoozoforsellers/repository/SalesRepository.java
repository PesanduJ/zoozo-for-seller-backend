package com.zoozo.zoozoforsellers.repository;

import com.zoozo.zoozoforsellers.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    List<Sales> findAllBySellerId(String sellerId);

    List<Sales> findBySoldDateBetween(LocalDate startDate, LocalDate endDate);
}
