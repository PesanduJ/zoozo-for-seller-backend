package com.zoozo.zoozoforsellers.repository;

import com.zoozo.zoozoforsellers.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
