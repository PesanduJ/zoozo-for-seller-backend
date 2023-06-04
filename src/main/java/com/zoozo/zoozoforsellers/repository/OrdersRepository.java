package com.zoozo.zoozoforsellers.repository;

import com.zoozo.zoozoforsellers.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findAllBySellerId(String sellerId);
}
