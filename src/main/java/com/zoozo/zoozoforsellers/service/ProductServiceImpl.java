package com.zoozo.zoozoforsellers.service;

import com.zoozo.zoozoforsellers.entity.Product;
import com.zoozo.zoozoforsellers.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    @Override
    public Product addProduct(Product product) throws IOException {
        if(product.getImageFile() != null){
            MultipartFile imageFile = product.getImageFile();
            String imageKey = imageService.saveImage(imageFile);
            product.setImageKey(imageKey);
        }
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(String productCode) {
        return productRepository.findById(productCode).get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProductById(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void removeProduct(String productCode) {
        productRepository.deleteById(productCode);
    }

    @Override
    public float getProductValueByProductCode(String productCode) {
        Product prod = productRepository.findById(productCode).get();
        return prod.getProductValue();

    }


}
