package com.zoozo.zoozoforsellers.service;

import com.zoozo.zoozoforsellers.entity.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    Product getProductById(String productCode);

    List<Product> getAllProducts();

    Product updateProductById(Product product);

    void removeProduct(String productCode);

    float getProductValueByProductCode(String productCode);

}
