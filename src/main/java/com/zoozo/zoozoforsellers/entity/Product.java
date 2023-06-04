package com.zoozo.zoozoforsellers.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
public class Product {

    @Id
    private String productCode;

    private String productName;

    @Lob
    private String description;

    private float sellingPrice;

    private String inStock;

    private float productValue;

    private float commission;

    private String imageKey; // AWS S3 image key

    @Transient
    private MultipartFile imageFile; // Transient field for image file
}
